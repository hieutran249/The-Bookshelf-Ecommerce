package com.hieutt.ecommerceweb.service.impl;

import com.hieutt.ecommerceweb.dto.CategoryDto;
import com.hieutt.ecommerceweb.entity.Book;
import com.hieutt.ecommerceweb.entity.Category;
import com.hieutt.ecommerceweb.exception.ResourceNotFoundException;
import com.hieutt.ecommerceweb.repository.CategoryRepository;
import com.hieutt.ecommerceweb.service.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper = new ModelMapper();
    private final Map<String, String> message;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
        this.message = new HashMap<>();
    }

    @Override
    public Map<String, String> createCategory(CategoryDto categoryDto) {
        if (categoryRepository.existsByName(categoryDto.getName())) {
            message.put("type", "error");
            message.put("detail", "Duplicated name! Please try again ⛔");
            return message;
        }
        categoryRepository.save(mapToEntity(categoryDto));
        message.put("type", "success");
        message.put("detail", "Successfully created 🎉");

        return message;
    }

    @Override
    public List<CategoryDto> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .map(category -> mapToDto(category))
                .collect(Collectors.toList());
    }

    @Override
    public List<CategoryDto> getAllCategories(int pageNo) {
        Pageable pageable = PageRequest.of(pageNo, 5);
        Page<Category> categoryPage = categoryRepository.findAll(pageable);
        List<Category> categories = categoryPage.getContent();
        return categories.stream()
                .map(category -> mapToDto(category))
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto getCategoryById(Long categoryId) {
        Category category = findCategoryById(categoryId);
        return mapToDto(category);
    }

    @Override
    public Map<String, String> updateCategory(Long categoryId, CategoryDto categoryDto) {
        Category category = findCategoryById(categoryId);
        if (categoryRepository.existsByName(categoryDto.getName()) &&
                !category.getName().equals(categoryDto.getName())) {
            message.put("type", "error");
            message.put("detail", "Duplicated name! Please try again ⛔");
            return message;
        }
        category.setName(categoryDto.getName());
        categoryRepository.save(category);
        message.put("type", "success");
        message.put("detail", "Successfully updated 🎉");
        return message;
    }

    @Override
    public void deleteCategory(Long categoryId) {
        Category category = findCategoryById(categoryId);
        category.setDeleted(true);
        categoryRepository.save(category);
    }

    @Override
    public void restoreCategory(Long categoryId) {
        Category category = findCategoryById(categoryId);
        category.setDeleted(false);
        categoryRepository.save(category);
    }

    private Category findCategoryById(Long id) {
        return categoryRepository
                .findById(id).orElseThrow(()
                -> new ResourceNotFoundException("Category", "id", id));
    }

    private CategoryDto mapToDto(Category category) {
        return modelMapper.map(category, CategoryDto.class);
    }
    private Category mapToEntity(CategoryDto categoryDto) {
        return modelMapper.map(categoryDto, Category.class);
    }
}
