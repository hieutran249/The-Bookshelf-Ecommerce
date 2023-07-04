package com.hieutt.ecommerceweb.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CartController {
    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/customer/cart")
    public String addToCart(Authentication authentication, Model model) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String user = userDetails.getUsername();
        model.addAttribute("user",user);
        return "customer/cart";
    }
}
