package com.ecommerce.controller;

import com.ecommerce.model.*;
import com.ecommerce.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/orders")
public class OrderController {

    @Autowired private OrderService orderService;
    @Autowired private UserService userService;
    @Autowired private CartService cartService;

    // ===== CHECKOUT PAGE =====
    @GetMapping("/checkout")
    public String checkout(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByEmail(userDetails.getUsername());
        Cart cart = cartService.getOrCreateCart(user);
        if (cart.getCartItems().isEmpty()) {
            return "redirect:/cart";
        }
        model.addAttribute("cart", cart);
        model.addAttribute("user", user);
        model.addAttribute("cartCount", cart.getTotalItems());
        return "checkout";
    }

    // ===== PLACE ORDER =====
    @PostMapping("/place")
    public String placeOrder(@RequestParam String shippingAddress,
                             @RequestParam(defaultValue = "Cash on Delivery") String paymentMethod,
                             @AuthenticationPrincipal UserDetails userDetails,
                             RedirectAttributes redirectAttributes) {
        User user = userService.findByEmail(userDetails.getUsername());
        try {
            Order order = orderService.placeOrder(user, shippingAddress, paymentMethod);
            redirectAttributes.addFlashAttribute("order", order);
            return "redirect:/orders/success/" + order.getId();
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMsg", e.getMessage());
            return "redirect:/cart";
        }
    }

    // ===== ORDER SUCCESS PAGE =====
    @GetMapping("/success/{orderId}")
    public String orderSuccess(@PathVariable Long orderId, Model model,
                               @AuthenticationPrincipal UserDetails userDetails,
                               RedirectAttributes redirectAttributes) {
        User user = userService.findByEmail(userDetails.getUsername());
        try {
            Order order = orderService.getOrderByIdForUser(orderId, user);
            model.addAttribute("order", order);
            model.addAttribute("cartCount", cartService.getCartItemCount(user));
            return "order-success";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMsg", "That order could not be found.");
            return "redirect:/profile";
        }
    }
}
