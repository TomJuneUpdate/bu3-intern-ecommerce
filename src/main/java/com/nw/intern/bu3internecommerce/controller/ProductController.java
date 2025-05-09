package com.nw.intern.bu3internecommerce.controller;

import com.nw.intern.bu3internecommerce.dto.ProductDto;
import com.nw.intern.bu3internecommerce.dto.request.AddProductRequest;
import com.nw.intern.bu3internecommerce.dto.response.ApiResponse;
import com.nw.intern.bu3internecommerce.entity.Product;
import com.nw.intern.bu3internecommerce.repository.ProductRepository;
import com.nw.intern.bu3internecommerce.service.ProductService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/products")
@AllArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final String uploadDir = "F:/A1/intern-ecommerce/uploads";
    private final ProductRepository productRepository;

    @GetMapping
    public ApiResponse<Page<ProductDto>> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        Page<ProductDto> products = productService.getAllProducts(page, size, sortBy, sortDir);

        return ApiResponse.ok(products);
    }

    @PostMapping
    public ApiResponse<ProductDto> createProduct(
            @RequestBody AddProductRequest addProductRequest) {
        return ApiResponse.ok(productService.createProduct(addProductRequest));
    }

    @PutMapping("/{id}")
    public ApiResponse<ProductDto> updateProduct(
            @PathVariable long id,
            @RequestBody ProductDto productDTO) {
        return ApiResponse.ok(productService.updateProduct(id, productDTO));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteProduct(
            @PathVariable long id) {
        productService.deleteProduct(id);
        return ApiResponse.ok();
    }

    @GetMapping("/{id}")
    public ApiResponse<ProductDto> findProduct(
            @PathVariable long id) {
        return ApiResponse.ok(productService.getProductById(id));
    }

    @PostMapping("/{id}/upload-images")
    public ApiResponse<Void> uploadMultipleImages(@PathVariable Long id,
                                                  @RequestParam("files") List<MultipartFile> files) throws IOException {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        if (files == null || files.isEmpty()) {
            return ApiResponse.fail("No files provided");
        }

        // Thư mục upload cần chính xác với resource handler
        Path uploadPath = Paths.get(uploadDir, "images", "products");
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                // Sanitize tên file
                String originalFilename = Paths.get(file.getOriginalFilename()).getFileName().toString()
                        .replaceAll("[^a-zA-Z0-9\\.\\-]", "_");

                String filename = UUID.randomUUID() + "_" + originalFilename;
                Path filePath = uploadPath.resolve(filename);

                // Lưu file
                file.transferTo(filePath.toFile());

                // Lưu đường dẫn tương đối để frontend sử dụng
                String relativePath = "/uploads/images/products/" + filename;
                product.getImageUrls().add(relativePath);
            }
        }

        productRepository.save(product);
        return ApiResponse.ok();
    }

}
