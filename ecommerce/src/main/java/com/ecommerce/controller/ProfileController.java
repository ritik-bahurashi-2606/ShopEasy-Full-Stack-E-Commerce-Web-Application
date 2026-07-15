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
import java.util.List;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    @Autowired private UserService userService;
    @Autowired private OrderService orderService;
    @Autowired private CartService cartService;

    // ===== VIEW PROFILE =====
    @GetMapping
    public String viewProfile(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByEmail(userDetails.getUsername());
        List<Order> orders = orderService.getOrdersByUser(user);
        model.addAttribute("user", user);
        model.addAttribute("orders", orders);
        model.addAttribute("cartCount", cartService.getCartItemCount(user));
        return "profile";
    }

    // ===== UPDATE PROFILE =====
    @PostMapping("/update")
    public String updateProfile(@ModelAttribute User updatedUser,
                                @AuthenticationPrincipal UserDetails userDetails,
                                RedirectAttributes redirectAttributes) {
        User user = userService.findByEmail(userDetails.getUsername());
        user.setFirstName(updatedUser.getFirstName());
        user.setLastName(updatedUser.getLastName());
        user.setPhone(updatedUser.getPhone());
        user.setAddress(updatedUser.getAddress());
        user.setCity(updatedUser.getCity());
        user.setState(updatedUser.getState());
        user.setPinCode(updatedUser.getPinCode());
        userService.updateUser(user);
        redirectAttributes.addFlashAttribute("successMsg", "Profile updated successfully!");
        return "redirect:/profile";
    }

    // ===== VIEW ORDER DETAIL =====
    @GetMapping("/orders/{orderId}")
    public String orderDetail(@PathVariable Long orderId, Model model,
                              @AuthenticationPrincipal UserDetails userDetails,
                              RedirectAttributes redirectAttributes) {
        User user = userService.findByEmail(userDetails.getUsername());
        try {
            Order order = orderService.getOrderByIdForUser(orderId, user);
            model.addAttribute("order", order);
            model.addAttribute("cartCount", cartService.getCartItemCount(user));
            return "order-detail";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMsg", "That order could not be found.");
            return "redirect:/profile";
        }
    }
}
