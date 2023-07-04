package com.hieutt.ecommerceweb.controller;

import com.hieutt.ecommerceweb.dto.BookDto;
import com.hieutt.ecommerceweb.dto.CategoryDto;
import com.hieutt.ecommerceweb.service.BookService;
import com.hieutt.ecommerceweb.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Controller
@RequestMapping("/admin/books")
public class BookController {
    private final BookService bookService;
    private final CategoryService categoryService;

    public BookController(BookService bookService, CategoryService categoryService) {
        this.bookService = bookService;
        this.categoryService = categoryService;
    }

    @GetMapping
    public String getAllBooks(Model model) {
        model.addAttribute("title", "Manage Books");
        List<BookDto> books = bookService.getAllBooks();
        model.addAttribute("books", books);
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("size", books.size());
        model.addAttribute("book", new BookDto());
        return "/admin/books";
    }

    @PostMapping
    public String createBook(@Valid @ModelAttribute(value = "book") BookDto bookDto,
                             BindingResult result,
                             @RequestParam(value = "product-image") MultipartFile image,
                             RedirectAttributes redirectAttributes) throws IOException {
        Map<String, String> message;
        if (result.hasErrors()) {
            List<FieldError> fieldErrors = result.getFieldErrors();
            List<String> fieldErrorsMsg = fieldErrors.stream()
                    .map((err) -> err.getDefaultMessage())
                    .toList();
            redirectAttributes.addFlashAttribute("fieldErrors", fieldErrorsMsg);
            return "redirect:/admin/books";
        }
        message = bookService.createBook(bookDto, image);
        redirectAttributes.addFlashAttribute("type", message.get("type"));
        redirectAttributes.addFlashAttribute("detail", message.get("detail"));

        return "redirect:/admin/books";
    }

    @GetMapping("/update-form/{id}")
    public String updateBookForm(@PathVariable(value = "id") Long bookId,
                                 Model model) {
        model.addAttribute("book", bookService.getBookById(bookId));
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("title", "Update Book");
        return "admin/edit-book";
    }

    @PostMapping("/update")
    public String updateBook(@Valid @ModelAttribute(value = "book") BookDto bookDto,
                             BindingResult result,
                             @RequestParam(value = "product-image") MultipartFile image,
                             RedirectAttributes redirectAttributes,
                             Model model) throws IOException {
        Map<String, String> message;
        if (result.hasErrors()) {
            System.out.println("validation errors");
            System.out.println(Objects.requireNonNull(result.getFieldError()).getDefaultMessage());
            model.addAttribute("categories", categoryService.getAllCategories());
            model.addAttribute("title", "Update Book");
            return "admin/edit-book";
        }
        System.out.println(image.isEmpty());
        message = bookService.updateBook(bookDto.getId(), bookDto, image);
        redirectAttributes.addFlashAttribute("type", message.get("type"));
        redirectAttributes.addFlashAttribute("detail", message.get("detail"));

        return "redirect:/admin/books";
    }

    @GetMapping("/delete/{id}")
    public String deleteBook(@PathVariable(value = "id") Long bookId,
                             RedirectAttributes redirectAttributes) {

        bookService.deleteBook(bookId);
        redirectAttributes.addFlashAttribute("message", "Successfully deleted 🎉");
        return "redirect:/admin/books";
    }

    @GetMapping("/restore/{id}")
    public String restoreCategory(@PathVariable(value = "id") Long bookId,
                                  RedirectAttributes redirectAttributes) {

        bookService.restoreBook(bookId);
        redirectAttributes.addFlashAttribute("message", "Successfully restored 🎉");
        return "redirect:/admin/books";
    }
}
