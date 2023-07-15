package com.hieutt.ecommerceweb.controller;

import com.hieutt.ecommerceweb.dto.PasswordDto;
import com.hieutt.ecommerceweb.dto.UserDto;
import com.hieutt.ecommerceweb.entity.User;
import com.hieutt.ecommerceweb.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
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
        model.addAttribute("title", "My account");
        return "customer/my-account";
    }

    @PostMapping("/customer/update-info")
    public String updateMyAccount(@Valid @ModelAttribute(value = "user") UserDto userDto,
                                  BindingResult result,
                                  RedirectAttributes redirectAttributes,
                                  Model model,
                                  Principal principal,
                                  @RequestParam(value = "user-image") MultipartFile image) throws IOException {
        if (result.hasErrors()) {
            String email = principal.getName();
            UserDto user = userService.getUserByEmail(email);
            model.addAttribute("user", user);
            model.addAttribute("password", new PasswordDto());
            return "customer/my-account";
        }

        Map<String, String> message = userService.updateUser(userDto.getId(), userDto, image);
        redirectAttributes.addFlashAttribute("type", message.get("type"));
        redirectAttributes.addFlashAttribute("detail", message.get("detail"));
        return "redirect:/customer/my-account";
    }

    @PostMapping("/customer/update-password")
    public String updateMyPassword(@RequestParam(value = "id") Long userId,
                                   @Valid @ModelAttribute(value = "password") PasswordDto passwordDto,
                                   BindingResult result,
                                   Principal principal,
                                   Model model,
                                   RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            String email = principal.getName();
            UserDto user = userService.getUserByEmail(email);
            model.addAttribute("user", user);
            model.addAttribute("password", new PasswordDto());
            return "customer/my-account";
        }
        Map<String, String> message = userService.updatePassword(userId, passwordDto);
        redirectAttributes.addFlashAttribute("type", message.get("type"));
        redirectAttributes.addFlashAttribute("detail", message.get("detail"));
        return "redirect:/customer/my-account";
    }
}
