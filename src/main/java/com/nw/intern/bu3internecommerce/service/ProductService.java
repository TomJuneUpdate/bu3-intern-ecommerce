package com.nw.intern.bu3internecommerce.service;

import com.nw.intern.bu3internecommerce.dto.ProductDto;
import com.nw.intern.bu3internecommerce.dto.request.AddProductRequest;
import com.nw.intern.bu3internecommerce.dto.response.ApiResponse;
import com.nw.intern.bu3internecommerce.entity.Product;
import org.springframework.data.domain.Page;

public interface ProductService {

    Page<ProductDto> getAllProducts(int page, int size, String sortBy, String sortDir);

    ProductDto getProductById(Long id);

    ProductDto createProduct(AddProductRequest request);

    ProductDto updateProduct(Long id, ProductDto productDto);

    void deleteProduct(Long id);

    ProductDto convertToDto(Product product);
}
