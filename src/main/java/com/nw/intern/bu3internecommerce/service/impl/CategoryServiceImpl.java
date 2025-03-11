package com.nw.intern.bu3internecommerce.service.impl;

import com.nw.intern.bu3internecommerce.dto.CategoryDto;
import com.nw.intern.bu3internecommerce.dto.request.AddCategoryRequest;
import com.nw.intern.bu3internecommerce.dto.response.ApiResponse;
import com.nw.intern.bu3internecommerce.entity.Category;
import com.nw.intern.bu3internecommerce.exception.ResourceNotFoundException;
import com.nw.intern.bu3internecommerce.repository.CategoryRepository;
import com.nw.intern.bu3internecommerce.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    public List<CategoryDto> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();

        // Trả về danh sách rỗng nếu không có danh mục nào
        if (categories.isEmpty()) {
            return new ArrayList<>();
        }

        List<CategoryDto> categoryDtos = categories.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        return categoryDtos;
    }


    @Override
    public CategoryDto getCategoryById(Long id) {
        return convertToDto(categoryRepository.findById(id).get());
    }

    @Override
    public CategoryDto createCategory(AddCategoryRequest request) {
        // Kiểm tra xem danh mục đã tồn tại chưa
        if (categoryRepository.existsByName(request.getName())){
            throw new IllegalArgumentException("Category name already exists");
        }
        // Tạo mã code tự động (Cách 1: Lấy max +1)
        String newCode = generateCategoryCode();
        // Tạo đối tượng Category mới
        Category newCategory = new Category();
        newCategory.setName(request.getName());
        newCategory.setCode(newCode);
        // Lưu vào DB
        newCategory = categoryRepository.save(newCategory);
        return convertToDto(newCategory);
    }

    @Override
    public void deleteCategoryById(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Category not found");
        }
        categoryRepository.deleteById(id);
    }

    /**
     * Chuyển đổi từ Entity -> DTO
     */
    private CategoryDto convertToDto(Category category) {
        return CategoryDto.builder()
                .code(category.getCode())
                .name(category.getName())
                .build();
    }

    /**
     * Sinh mã danh mục tự động (0001 - 9999)
     */
    private String generateCategoryCode() {
        // Lấy mã lớn nhất từ DB (nếu có)
        String maxCode = categoryRepository.findMaxCategoryCode();

        if (maxCode == null) {
            return "0001"; // Nếu chưa có dữ liệu, bắt đầu từ "0001"
        }

        // Chuyển đổi mã hiện tại sang số nguyên, sau đó tăng lên 1
        int nextCode = Integer.parseInt(maxCode) + 1;

        // Định dạng lại về dạng "0001"
        return String.format("%04d", nextCode);
    }

}
