package com.ecommerce.controller;

import com.ecommerce.model.*;
import com.ecommerce.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {

    @Autowired private ProductService productService;
    @Autowired private UserService userService;
    @Autowired private CartService cartService;

    // ===== PRODUCT LIST =====
    @GetMapping
    public String listProducts(@RequestParam(required = false) String category,
                               @RequestParam(required = false) String search,
                               Model model,
                               @AuthenticationPrincipal UserDetails userDetails) {

        List<Product> products;
        if (search != null && !search.isBlank()) {
            products = productService.searchProducts(search);
            model.addAttribute("searchQuery", search);
        } else if (category != null && !category.isBlank()) {
            products = productService.getProductsByCategory(category);
            model.addAttribute("selectedCategory", category);
        } else {
            products = productService.getAllProducts();
        }

        model.addAttribute("products", products);
        model.addAttribute("categories", List.of("Electronics", "Clothing", "Footwear", "Kitchen", "Books", "Sports", "Toys", "Grocery"));

        if (userDetails != null) {
            User user = userService.findByEmail(userDetails.getUsername());
            model.addAttribute("cartCount", cartService.getCartItemCount(user));
        }

        return "products";
    }

    // ===== PRODUCT DETAIL =====
    @GetMapping("/{id}")
    public String productDetail(@PathVariable Long id, Model model,
                                @AuthenticationPrincipal UserDetails userDetails) {
        Product product = productService.getProductById(id);
        model.addAttribute("product", product);

        if (userDetails != null) {
            User user = userService.findByEmail(userDetails.getUsername());
            model.addAttribute("cartCount", cartService.getCartItemCount(user));
        }

        return "product-detail";
    }
}
