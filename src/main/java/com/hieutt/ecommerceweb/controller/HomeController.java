package com.hieutt.ecommerceweb.controller;

import com.hieutt.ecommerceweb.entity.CartItem;
import com.hieutt.ecommerceweb.entity.ShoppingCart;
import com.hieutt.ecommerceweb.service.BookService;
import com.hieutt.ecommerceweb.service.CategoryService;
import com.hieutt.ecommerceweb.service.ShoppingCartService;
import com.hieutt.ecommerceweb.utils.Constants;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.w3c.dom.stylesheets.LinkStyle;

import java.security.Principal;
import java.util.List;

@Controller
public class HomeController {
    private final BookService bookService;
    private final CategoryService categoryService;
    private final ShoppingCartService cartService;

    public HomeController(BookService bookService, CategoryService categoryService, ShoppingCartService cartService) {
        this.bookService = bookService;
        this.categoryService = categoryService;
        this.cartService = cartService;
    }

    @GetMapping(value = {"/", "/home"})
    public String home(Model model, HttpSession session, Principal principal) {
//        if (authentication != null) {
//            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//            String user = userDetails.getUsername();
//            model.addAttribute("user",user);
//        }
        model.addAttribute("title", "Homepage");
        if (principal != null) {
            ShoppingCart cart = cartService.getCartByUser(principal);
            session.setAttribute("cart", cart);
        }
        return "customer/home";
    }

    @GetMapping(value = { "/index", "/menu"})
    public String index(Authentication authentication,
                        Model model,
                        @RequestParam(value = "sortBy", defaultValue = Constants.DEFAULT_SORT_BY, required = false) String sortBy,
                        @RequestParam(value = "sortDir", defaultValue = Constants.DEFAULT_SORT_DIRECTION, required = false) String sortDir,
                        Principal principal,
                        HttpSession session) {
        if (principal != null) {
            ShoppingCart cart = cartService.getCartByUser(principal);
            session.setAttribute("cart", cart);
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
