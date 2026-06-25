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
@RequestMapping("/cart")
public class CartController {

    @Autowired private CartService cartService;
    @Autowired private UserService userService;

    // ===== VIEW CART =====
    @GetMapping
    public String viewCart(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByEmail(userDetails.getUsername());
        Cart cart = cartService.getOrCreateCart(user);
        model.addAttribute("cart", cart);
        model.addAttribute("cartCount", cart.getTotalItems());
        return "cart";
    }

    // ===== ADD TO CART =====
    @PostMapping("/add")
    public String addToCart(@RequestParam Long productId,
                            @RequestParam(defaultValue = "1") int quantity,
                            @AuthenticationPrincipal UserDetails userDetails,
                            RedirectAttributes redirectAttributes) {
        User user = userService.findByEmail(userDetails.getUsername());
        cartService.addToCart(user, productId, quantity);
        redirectAttributes.addFlashAttribute("successMsg", "Product added to cart!");
        return "redirect:/products/" + productId;
    }

    // ===== REMOVE FROM CART =====
    @PostMapping("/remove/{itemId}")
    public String removeFromCart(@PathVariable Long itemId,
                                 @AuthenticationPrincipal UserDetails userDetails,
                                 RedirectAttributes redirectAttributes) {
        User user = userService.findByEmail(userDetails.getUsername());
        cartService.removeFromCart(user, itemId);
        redirectAttributes.addFlashAttribute("successMsg", "Item removed from cart.");
        return "redirect:/cart";
    }

    // ===== UPDATE QUANTITY =====
    @PostMapping("/update/{itemId}")
    public String updateQuantity(@PathVariable Long itemId,
                                 @RequestParam int quantity,
                                 @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByEmail(userDetails.getUsername());
        cartService.updateQuantity(user, itemId, quantity);
        return "redirect:/cart";
    }
}