package com.nw.intern.bu3internecommerce.controller;

import com.nw.intern.bu3internecommerce.dto.CategoryDto;
import com.nw.intern.bu3internecommerce.dto.request.AddCategoryRequest;
import com.nw.intern.bu3internecommerce.dto.response.ApiResponse;
import com.nw.intern.bu3internecommerce.service.CategoryService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}")
@AllArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping("/getAllCategories")
    public ApiResponse<List<CategoryDto>> getAllCategories() {
        return ApiResponse.ok(categoryService.getAllCategories());
    }

    @GetMapping("/categories/{id}")
    public ApiResponse<CategoryDto> getCategoryById(@PathVariable Long id) {
        return ApiResponse.ok(categoryService.getCategoryById(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/category")
    public ApiResponse<CategoryDto> createCategory(@RequestBody AddCategoryRequest request) {
        return ApiResponse.ok(categoryService.createCategory(request));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/admin/category{id}")
    public ApiResponse<Void> deleteCategory(@PathVariable Long id) {
        return ApiResponse.ok();
    }
}
