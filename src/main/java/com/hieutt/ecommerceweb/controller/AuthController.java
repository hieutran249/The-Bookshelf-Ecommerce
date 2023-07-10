package com.hieutt.ecommerceweb.controller;

import com.hieutt.ecommerceweb.dto.RegisterDto;
import com.hieutt.ecommerceweb.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Map;

@Controller
public class AuthController {
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/login-admin")
    public String adminLoginForm(Model model) {
        model.addAttribute("title", "Admin Login");
        return "admin/login";
    }

    @GetMapping("/register-admin")
    public String adminRegisterForm(Model model) {
        model.addAttribute("title", "Admin Register");
        model.addAttribute("register", new RegisterDto());
        return "admin/register";
    }

    @GetMapping("/forgot-password-admin")
    public String adminForgotPasswordForm(Model model) {
        model.addAttribute("title", "Admin Forgot Password");
        return "admin/forgot-password";
    }

    @GetMapping("/login")
    public String customerLoginForm(Model model) {
        model.addAttribute("title", "Login");
        model.addAttribute("register", new RegisterDto());
        return "customer/login";
    }

    @GetMapping("/register")
    public String customerRegisterForm(Model model) {
        model.addAttribute("title", "Register");
        model.addAttribute("register", new RegisterDto());
        return "customer/register";
    }

//    @GetMapping("/forgot-password")
//    public String customerForgotPasswordForm(Model model) {
//        model.addAttribute("title", "Forgot Password");
//        return "customer/forgot-password";
//    }

    @PostMapping("/do-register-admin")
    public String registerAdmin(@Valid @ModelAttribute(value = "register") RegisterDto registerDto,
                                BindingResult result,
                                Model model) {
        if (result.hasErrors()) {
            return "admin/register";
        }
        Map<String, String> message = userService.createAdmin(registerDto);
        model.addAttribute("type", message.get("type"));
        model.addAttribute("detail", message.get("detail"));
        return "admin/register";
    }

    @PostMapping("/register")
    public String registerCustomer(@Valid @ModelAttribute(value = "register") RegisterDto registerDto,
                                   BindingResult result,
                                   Model model) {
        if (result.hasErrors()) {
            return "customer/register";
        }
        Map<String, String> message = userService.createCustomer(registerDto);
        model.addAttribute("type", message.get("type"));
        model.addAttribute("detail", message.get("detail"));
        return "customer/register";
    }
}
