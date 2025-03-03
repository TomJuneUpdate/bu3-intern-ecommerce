package com.nw.intern.bu3internecommerce.controller;

import com.nw.intern.bu3internecommerce.dto.ProductDto;
import com.nw.intern.bu3internecommerce.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/products")
@AllArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<?> getAllProducts() {
        return ResponseEntity.ok("");
    }

    // Thêm sản phẩm mới
    @PostMapping
    public ResponseEntity<?> addProduct(@RequestBody ProductDto productDTO) {
        return ResponseEntity.ok("");
    }

    // Sửa sản phẩm
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable long id, @RequestBody ProductDto productDTO) {
        return ResponseEntity.ok("");
    }

    // Xóa sản phẩm theo ID
    @DeleteMapping("/{id}")
    public ResponseEntity<?> removeProduct(@PathVariable long id) {
        return ResponseEntity.ok("");
    }

    // Tìm sản phẩm theo ID
    @GetMapping("/{id}")
    public ResponseEntity<?> findProduct(@PathVariable long id) {
        return ResponseEntity.ok("");
    }
}
