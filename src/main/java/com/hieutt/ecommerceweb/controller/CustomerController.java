package com.hieutt.ecommerceweb.controller;

import com.hieutt.ecommerceweb.dto.PasswordDto;
import com.hieutt.ecommerceweb.dto.UserDto;
import com.hieutt.ecommerceweb.entity.User;
import com.hieutt.ecommerceweb.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.Map;

@Controller
public class CustomerController {
    private final UserService userService;

    public CustomerController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/admin/customers")
    public String getAllCustomers() {
        return "/admin/customer";
    }

    @GetMapping("/customer/my-account")
    public String getMyAccount(Model model, Principal principal) {
        String email = principal.getName();
        UserDto user = userService.getUserByEmail(email);
        model.addAttribute("user", user);
        model.addAttribute("password", new PasswordDto());
        return "customer/my-account";
    }

    @PostMapping("/customer/update-account")
    public String updateMyAccount(@RequestAttribute(value = "user") UserDto userDto,
                                  RedirectAttributes redirectAttributes) {
        Map<String, String> message = userService.updateUser(userDto.getId(), userDto);
        redirectAttributes.addFlashAttribute("error", message.get("error"));
        redirectAttributes.addFlashAttribute("detail", message.get("detail"));
        return "redirect:/customer/my-account";
    }

    @PostMapping("/customer/update-password")
    public String updateMyPassword(@RequestParam(value = "id") Long userId,
                                   @RequestAttribute(value = "password") PasswordDto passwordDto,
                                   RedirectAttributes redirectAttributes) {
        Map<String, String> message = userService.updatePassword(userId, passwordDto);
        redirectAttributes.addFlashAttribute("error", message.get("error"));
        redirectAttributes.addFlashAttribute("detail", message.get("detail"));
        return "redirect:/customer/my-account";
    }
}
