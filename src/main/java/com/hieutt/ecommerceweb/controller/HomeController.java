package com.hieutt.ecommerceweb.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/")
    public String home(Authentication authentication, Model model) {
        boolean authenticated = false;
        if (authentication != null) {
            authenticated = true;
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String user = userDetails.getUsername();
            model.addAttribute("user",user);
        }
        System.out.println(authenticated);
        return "customer/index";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    public String admin(Model model) {
        model.addAttribute("title", "Admin");
        return "admin/index";
    }
}
