package com.hieutt.ecommerceweb.controller;

import com.hieutt.ecommerceweb.dto.ChangePasswordDto;
import com.hieutt.ecommerceweb.dto.RegisterDto;
import com.hieutt.ecommerceweb.dto.ResetPasswordDto;
import com.hieutt.ecommerceweb.dto.UserDto;
import com.hieutt.ecommerceweb.entity.User;
import com.hieutt.ecommerceweb.service.EmailSenderService;
import com.hieutt.ecommerceweb.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @GetMapping("/register-admin")
    public String adminRegisterForm(Model model) {
        model.addAttribute("title", "Admin Register");
        model.addAttribute("register", new RegisterDto());
        return "admin/register";
    }

    @PreAuthorize("hasRole('SUPER_ADMIN')")
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

    @GetMapping("/forgot-password")
    public String adminForgotPasswordForm(Model model) {
        model.addAttribute("title", "Forgot Password");
        return "forgot-password";
    }

    @GetMapping("/request-reset-password")
    public String requestResetPassword(@RequestParam(value = "email") String email,
                                RedirectAttributes redirectAttributes) {
        Map<String, String> message = userService.requestResetPassword(email);
        redirectAttributes.addFlashAttribute("type", message.get("type"));
        redirectAttributes.addFlashAttribute("detail", message.get("detail"));
        return "redirect:/forgot-password";
    }

    @GetMapping("/reset-password-form/{id}")
    public String resetPasswordForm(@PathVariable(value = "id") Long userId,
                                    Model model) {
        model.addAttribute("userId", userId);
        model.addAttribute("resetPassword", new ResetPasswordDto());
        return "forgot-password-form";
    }

    @PostMapping("/reset-password/{id}")
    public String resetPassword(@PathVariable(value = "id") Long userId,
                                @Valid @ModelAttribute(value = "resetPassword") ResetPasswordDto resetPasswordDto,
                                BindingResult result,
                                RedirectAttributes redirectAttributes,
                                Model model) {
        if (result.hasErrors()) {
            model.addAttribute("userId", userId);
            model.addAttribute("resetPassword", new ResetPasswordDto());
            return "forgot-password-form";
        }
        Map<String, String> message = userService.resetPassword(userId, resetPasswordDto);
        redirectAttributes.addFlashAttribute("type", message.get("type"));
        redirectAttributes.addFlashAttribute("detail", message.get("detail"));
        return "redirect:/reset-password-form/" + userId;
    }
}
