package com.hieutt.ecommerceweb.controller;

import com.hieutt.ecommerceweb.service.BookService;
import com.hieutt.ecommerceweb.service.CategoryService;
import com.hieutt.ecommerceweb.utils.Constants;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CustomerBookController {
    private final BookService bookService;
    private final CategoryService categoryService;

    public CustomerBookController(BookService bookService, CategoryService categoryService) {
        this.bookService = bookService;
        this.categoryService = categoryService;
    }

    @GetMapping("/shop")
    public String getAllBooks(
            Model model,
            @RequestParam(value = "sortBy", defaultValue = Constants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = Constants.DEFAULT_SORT_DIRECTION, required = false) String sortDir) {
        model.addAttribute("title", "Shop");
        model.addAttribute("categories", categoryService.getAllAvailableCategories());
        model.addAttribute("books", bookService.getAllAvailableBooks(sortBy, sortDir));
        return "customer/shop";
    }
}
