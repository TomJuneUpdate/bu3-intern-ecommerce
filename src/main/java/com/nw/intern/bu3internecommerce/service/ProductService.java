package com.nw.intern.bu3internecommerce.service;

import com.nw.intern.bu3internecommerce.dto.ProductDto;
import org.springframework.data.domain.Page;

public interface ProductService {
    /**
     *
     * @param page
     * @param size
     * @param sortBy
     * @param sortDir
     * @return
     */
    Page<ProductDto> getAllProducts(int page, int size, String sortBy, String sortDir);

    /**
     *
     * @param id
     * @return
     */
    ProductDto getProductById(Long id);

    /**
     *
     * @param productDto
     * @return
     */
    ProductDto createProduct(ProductDto productDto);

    /**
     *
     * @param id
     * @param productDto
     * @return
     */
    ProductDto updateProduct(Long id, ProductDto productDto);

    /**
     *
     * @param id
     */
    void deleteProduct(Long id);
}
