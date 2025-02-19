package com.nw.intern.bu3internecommerce.repository;

import com.nw.intern.bu3internecommerce.entity.Product;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ProductRepository {
    private final List<Product> productList = new ArrayList<>();
    private long nextId = 1; // ID tự tăng

    @PostConstruct
    public void init() {
        productList.add(new Product(nextId++, "Áo Thun", 199000.0, "Thời Trang", 50));
        productList.add(new Product(nextId++, "Quần Jeans", 499000.0, "Thời Trang", 30));
        productList.add(new Product(nextId++, "Giày Sneaker", 899000.0, "Giày Dép", 20));
        productList.add(new Product(nextId++, "Balo Laptop", 399000.0, "Phụ Kiện", 15));
        productList.add(new Product(nextId++, "Đồng Hồ Nam", 1299000.0, "Phụ Kiện", 10));
    }

    public List<Product> getAllProducts() {
        return productList;
    }

    public Product addProduct(Product product) {
        product.setId(nextId++); // Tự động tăng ID
        productList.add(product);
        return product;
    }

    public boolean removeProductById(long id) {
        return productList.removeIf(product -> product.getId() == id);
    }

    public Product findById(long id) {
        return productList.stream()
                .filter(product -> product.getId() == id)
                .findFirst()
                .orElse(null);
    }
    // Cập nhật sản phẩm
    public Product updateProduct(Product updatedProduct) {
        for (Product product : productList) {
            if (product.getId() == updatedProduct.getId()) {
                product.setName(updatedProduct.getName());
                product.setPrice(updatedProduct.getPrice());
                product.setCategory(updatedProduct.getCategory());
                product.setStock(updatedProduct.getStock());
                return product;
            }
        }
        return null;
    }
}