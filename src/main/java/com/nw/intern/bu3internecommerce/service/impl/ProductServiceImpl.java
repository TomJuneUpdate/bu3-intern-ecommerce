package com.nw.intern.bu3internecommerce.service.impl;

import com.nw.intern.bu3internecommerce.config.Translator;
import com.nw.intern.bu3internecommerce.dto.ProductDto;
import com.nw.intern.bu3internecommerce.entity.Product;
import com.nw.intern.bu3internecommerce.exception.ResourceNotFoundException;
import com.nw.intern.bu3internecommerce.repository.ProductRepository;
import com.nw.intern.bu3internecommerce.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    @Override
    public ProductDto getProductById(Long id) {
        return productRepository.findById(id).map(product ->
                        ProductDto.builder()
                                .name(product.getName())
                                .price(product.getPrice())
                                .description(product.getDescription())
                                .category(product.getCategory())
                                .imageUrl(product.getImageUrl())
                                .stock(product.getStock())
                                .build())
                .orElseThrow(() -> new ResourceNotFoundException(Translator.toLocale("product.notfound") + id));
    }

    @Override
    public ProductDto createProduct(ProductDto productDto) {
        return Optional.of(productDto).map(dto -> {
                    Product product = new Product();
                    product.setName(dto.getName());
                    product.setPrice(dto.getPrice());
                    product.setDescription(dto.getDescription());
                    product.setCategory(dto.getCategory());
                    product.setImageUrl(dto.getImageUrl());
                    product.setStock(dto.getStock());
                    return product;
                })
                .map(productRepository::save)
                .map(dto ->
                        ProductDto.builder()
                                .name(dto.getName())
                                .price(dto.getPrice())
                                .description(dto.getDescription())
                                .category(dto.getCategory())
                                .imageUrl(dto.getImageUrl())
                                .stock(dto.getStock())
                                .build())
                .orElseThrow(() -> new RuntimeException(Translator.toLocale("product.createError")));
    }

    @Override
    public ProductDto updateProduct(Long id, ProductDto productDto) {
        return productRepository.findById(id).map(product -> {
                    product.setName(productDto.getName());
                    product.setPrice(productDto.getPrice());
                    product.setDescription(productDto.getDescription());
                    product.setCategory(productDto.getCategory());
                    product.setImageUrl(productDto.getImageUrl());
                    product.setStock(productDto.getStock());
                    return product;
                })
                .map(productRepository::save)
                .map(dto ->
                        ProductDto.builder()
                                .name(dto.getName())
                                .price(dto.getPrice())
                                .description(dto.getDescription())
                                .category(dto.getCategory())
                                .imageUrl(dto.getImageUrl())
                                .stock(dto.getStock())
                                .build())
                .orElseThrow(() -> new RuntimeException(Translator.toLocale("product.fail")));
    }

    @Override
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(()
                        -> new ResourceNotFoundException(Translator.toLocale("product.notfound") + id));
        productRepository.delete(product);
    }

    public Page<ProductDto> getAllProducts(int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return productRepository.findAll(pageable).map(product ->
                ProductDto.builder()
                        .name(product.getName())
                        .price(product.getPrice())
                        .description(product.getDescription())
                        .stock(product.getStock())
                        .category(product.getCategory())
                        .imageUrl(product.getImageUrl())
                        .build()
        );
    }

}
