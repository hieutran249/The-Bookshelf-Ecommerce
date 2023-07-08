package com.hieutt.ecommerceweb.controller;

import com.hieutt.ecommerceweb.dto.BookDto;
import com.hieutt.ecommerceweb.service.BookService;
import com.hieutt.ecommerceweb.service.CategoryService;
import com.hieutt.ecommerceweb.utils.Constants;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequestMapping("/shop")
@Controller
public class CustomerBookController {
    private final BookService bookService;
    private final CategoryService categoryService;

    public CustomerBookController(BookService bookService, CategoryService categoryService) {
        this.bookService = bookService;
        this.categoryService = categoryService;
    }

    @GetMapping
    public String getAllBooks(
            Model model,
            @RequestParam(value = "sortBy", defaultValue = Constants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = Constants.DEFAULT_SORT_DIRECTION, required = false) String sortDir) {
        model.addAttribute("title", "Shop");
        model.addAttribute("categories", categoryService.getAllAvailableCategories());
        model.addAttribute("books", bookService.getAllAvailableBooks(sortBy, sortDir));
        return "customer/shop";
    }

    @GetMapping("/{categoryId}")
    public String getBooksByCategory(@PathVariable("categoryId") Long categoryId, Model model) {
        model.addAttribute("title", "Shop");
        model.addAttribute("categories", categoryService.getAllAvailableCategories());
        model.addAttribute("books", bookService.getAllAvailableBooksByCategory(categoryId));
        return "customer/shop";
    }

    @GetMapping("/search")
    public String searchBooks(@RequestParam(value = "keyword") String keyword,
                              Model model) {
        model.addAttribute("title", "Shop");
        model.addAttribute("categories", categoryService.getAllAvailableCategories());
        model.addAttribute("books", bookService.searchAvailableBooks(keyword));
        return "customer/shop";
    }

    @GetMapping("/detail/{id}")
    public String getBookById(@PathVariable("id") Long bookId, Model model) {
        BookDto book = bookService.getBookById(bookId);
        model.addAttribute("book", book);
        model.addAttribute("relatedBooks", bookService.getAllAvailableRelatedBooks(book.getCategory(), book));
        model.addAttribute("title", book.getTitle());
        return "customer/product-detail";
    }

}
