package com.nw.intern.bu3internecommerce.controller;

import com.nw.intern.bu3internecommerce.model.dto.request.ProductRequest;
import com.nw.intern.bu3internecommerce.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
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
        try {
            return new ResponseEntity<>(productService.getAllProducts(), HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    // Thêm sản phẩm mới
    @PostMapping
    public ResponseEntity<?> addProduct(@RequestBody ProductRequest productDTO) {
        try {
            productService.addProduct(productDTO);
            return ResponseEntity.ok("Product added successfully");
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Sửa sản phẩm
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable long id, @RequestBody ProductRequest productDTO) {
        try {
            productService.updateProduct(id, productDTO);
            return ResponseEntity.ok("Product updated successfully");
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Xóa sản phẩm theo ID
    @DeleteMapping("/{id}")
    public ResponseEntity<?> removeProduct(@PathVariable long id) {
        try {
            productService.removeProduct(id);
            return ResponseEntity.ok("Product removed successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    // Tìm sản phẩm theo ID
    @GetMapping("/{id}")
    public ResponseEntity<?> findProduct(@PathVariable long id) {
        try {
            productService.findById(id);
            return ResponseEntity.ok("Product found successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
