package com.hieutt.ecommerceweb.controller;

import com.hieutt.ecommerceweb.service.BookService;
import com.hieutt.ecommerceweb.service.CategoryService;
import com.hieutt.ecommerceweb.utils.Constants;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {
    private final BookService bookService;
    private final CategoryService categoryService;

    public HomeController(BookService bookService, CategoryService categoryService) {
        this.bookService = bookService;
        this.categoryService = categoryService;
    }

    @GetMapping(value = {"/", "/home"})
    public String home(Authentication authentication, Model model) {
        if (authentication != null) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String user = userDetails.getUsername();
            model.addAttribute("user",user);
        }
        model.addAttribute("title", "Homepage");
        return "customer/home";
    }

    @GetMapping(value = { "/index", "/menu"})
    public String index(Authentication authentication,
                        Model model,
                        @RequestParam(value = "sortBy", defaultValue = Constants.DEFAULT_SORT_BY, required = false) String sortBy,
                        @RequestParam(value = "sortDir", defaultValue = Constants.DEFAULT_SORT_DIRECTION, required = false) String sortDir) {
        if (authentication != null) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String user = userDetails.getUsername();
            model.addAttribute("user",user);
        }
        model.addAttribute("title", "Menu");
        model.addAttribute("categories", categoryService.getAllAvailableCategories());
        model.addAttribute("books", bookService.getAllAvailableBooks(sortBy, sortDir));
        return "customer/index";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    public String admin(Model model) {
        model.addAttribute("title", "Admin");
        return "admin/index";
    }
}
