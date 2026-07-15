package com.ecommerce.service;

import com.ecommerce.model.*;
import com.ecommerce.repository.OrderRepository;
import com.ecommerce.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    @Autowired private OrderRepository orderRepository;
    @Autowired private ProductRepository productRepository;
    @Autowired private CartService cartService;

    // Place an order from cart
    @Transactional
    public Order placeOrder(User user, String shippingAddress, String paymentMethod) {
        Cart cart = cartService.getOrCreateCart(user);

        if (cart.getCartItems().isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        // ===== STOCK CHECK (fail fast before touching anything) =====
        for (CartItem cartItem : cart.getCartItems()) {
            Product product = cartItem.getProduct();
            int available = product.getStock() != null ? product.getStock() : 0;
            if (available < cartItem.getQuantity()) {
                throw new RuntimeException(
                        "Not enough stock for \"" + product.getName() + "\". Only " + available + " left.");
            }
        }

        Order order = new Order();
        order.setUser(user);
        order.setShippingAddress(shippingAddress);
        order.setPaymentMethod(paymentMethod);
        order.setStatus("PLACED");
        order.setTotalAmount(cart.getTotalPrice());

        List<OrderItem> orderItems = new ArrayList<>();
        for (CartItem cartItem : cart.getCartItems()) {
            Product product = cartItem.getProduct();

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(product.getPrice()); // snapshot price at purchase time
            orderItems.add(orderItem);

            // ===== STOCK DECREMENT =====
            product.setStock(product.getStock() - cartItem.getQuantity());
            productRepository.save(product);
        }
        order.setOrderItems(orderItems);

        Order savedOrder = orderRepository.save(order);

        // Clear cart after placing order
        cartService.clearCart(user);

        return savedOrder;
    }

    public List<Order> getOrdersByUser(User user) {
        return orderRepository.findByUserOrderByCreatedAtDesc(user);
    }

    /**
     * Returns an order only if it belongs to the given user.
     * Deliberately throws the same generic "not found" error whether the
     * order doesn't exist OR belongs to someone else — this avoids leaking
     * whether a given order ID exists to users who don't own it (IDOR fix).
     */
    public Order getOrderByIdForUser(Long id, User user) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (!order.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Order not found");
        }
        return order;
    }
}
