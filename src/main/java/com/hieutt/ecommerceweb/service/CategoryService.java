package com.hieutt.ecommerceweb.service;

import com.hieutt.ecommerceweb.dto.CategoryDto;

import java.util.List;
import java.util.Map;

public interface CategoryService {
    Map<String, String> createCategory(CategoryDto categoryDto);
    List<CategoryDto> getAllCategories();
    CategoryDto getCategoryById(Long categoryId);
    Map<String, String> updateCategory(Long id, CategoryDto categoryDto);
    void deleteCategory(Long categoryId);
    void restoreCategory(Long categoryId);
}
