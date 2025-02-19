package com.nw.intern.bu3internecommerce.service;

import com.nw.intern.bu3internecommerce.dto.request.ProductRequest;
import com.nw.intern.bu3internecommerce.entity.Product;
import com.nw.intern.bu3internecommerce.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    public List<Product> getAllProducts() {
       return productRepository.getAllProducts();
    }

    public Product getProductById(int id) {
        return productRepository.findById(id);
    }
    public Product addProduct(ProductRequest productDTO) {
        Product product = new Product();
        product.setName(productDTO.getName());
        product.setPrice(productDTO.getPrice());
        product.setCategory(productDTO.getCategory());
        product.setStock(productDTO.getStock());
        return productRepository.addProduct(product);
    }
    public Product updateProduct(long id, ProductRequest productDTO) {
        Product product = productRepository.findById(id);
        if (product != null) {
            product.setName(productDTO.getName());
            product.setPrice(productDTO.getPrice());
            product.setCategory(productDTO.getCategory());
            product.setStock(productDTO.getStock());
            return productRepository.updateProduct(product);
        }
        return null;
    }
    // Xóa sản phẩm
    public boolean removeProduct(long id) {
        return productRepository.removeProductById(id);
    }
    // Tìm sản phẩm theo ID
    public Product findById(long id) {
        return productRepository.findById(id);
    }
}
