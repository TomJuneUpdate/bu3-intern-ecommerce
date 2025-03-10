package com.nw.intern.bu3internecommerce.service;

import com.nw.intern.bu3internecommerce.dto.CategoryDto;
import com.nw.intern.bu3internecommerce.dto.request.AddCategoryRequest;
import com.nw.intern.bu3internecommerce.dto.response.ApiResponse;

import java.util.List;

public interface CategoryService {
    ApiResponse<List<CategoryDto>> getAllCategories();
    ApiResponse<CategoryDto> getCategoryById(Long id);
    ApiResponse<CategoryDto> createCategory(AddCategoryRequest request);
    ApiResponse<Void> deleteCategoryById(Long id);
}
