package com.hieutt.ecommerceweb.controller;

import com.hieutt.ecommerceweb.dto.CategoryDto;
import com.hieutt.ecommerceweb.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/categories")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public String getAllCategories(Model model) {
        model.addAttribute("title", "Manage Categories");
        List<CategoryDto> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        model.addAttribute("size", categories.size());
        model.addAttribute("category", new CategoryDto());
        return "/admin/categories";
    }

    @PostMapping
    public String createCategory(@Valid @ModelAttribute(value = "category") CategoryDto categoryDto,
                                 BindingResult result,
                                 RedirectAttributes redirectAttributes) {
        Map<String, String> message;
        if (result.hasErrors()) {
            System.out.println("validation errors");
            List<FieldError> fieldErrors = result.getFieldErrors();
            List<String> fieldErrorsMsg = fieldErrors.stream()
                    .map((err) -> err.getDefaultMessage())
                    .toList();
            redirectAttributes.addFlashAttribute("fieldErrors", fieldErrorsMsg);
            return "redirect:/admin/categories";
        }
        message = categoryService.createCategory(categoryDto);
        redirectAttributes.addFlashAttribute("type", message.get("type"));
        redirectAttributes.addFlashAttribute("detail", message.get("detail"));

        return "redirect:/admin/categories";
    }

    @GetMapping("/update-form/{id}")
    public String updateCategoryForm(@PathVariable(value = "id") Long categoryId,
                                     Model model) {
        model.addAttribute("category", categoryService.getCategoryById(categoryId));
        model.addAttribute("title", "Update Category");
        return "admin/edit-category";
    }

    @PostMapping("/update")
    public String updateCategory(@Valid @ModelAttribute(value = "category") CategoryDto categoryDto,
                                 BindingResult result,
                                 RedirectAttributes redirectAttributes) {
        Map<String, String> message;
        if (result.hasErrors()) {
            System.out.println("validation errors");
            return "admin/edit-category";
        }
        message = categoryService.updateCategory(categoryDto.getId(), categoryDto);
        redirectAttributes.addFlashAttribute("type", message.get("type"));
        redirectAttributes.addFlashAttribute("detail", message.get("detail"));

        return "redirect:/admin/categories";
    }

    @GetMapping("/delete/{id}")
    public String deleteCategory(@PathVariable(value = "id") Long categoryId,
                                 RedirectAttributes redirectAttributes) {

        categoryService.deleteCategory(categoryId);
        redirectAttributes.addFlashAttribute("message", "Successfully deleted 🎉");
        return "redirect:/admin/categories";
    }

    @GetMapping("/restore/{id}")
    public String restoreCategory(@PathVariable(value = "id") Long categoryId,
                                 Model model,
                                 RedirectAttributes redirectAttributes) {

        categoryService.restoreCategory(categoryId);
        redirectAttributes.addFlashAttribute("message", "Successfully restored 🎉");
        return "redirect:/admin/categories";
    }
}
