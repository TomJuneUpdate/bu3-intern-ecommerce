package com.nw.intern.bu3internecommerce.service;

import com.nw.intern.bu3internecommerce.dto.CategoryDto;
import com.nw.intern.bu3internecommerce.dto.request.AddCategoryRequest;
import com.nw.intern.bu3internecommerce.dto.response.ApiResponse;

import java.util.List;

public interface CategoryService {
    List<CategoryDto> getAllCategories();
    CategoryDto getCategoryById(Long id);
    CategoryDto createCategory(AddCategoryRequest request);
    void deleteCategoryById(Long id);
}
