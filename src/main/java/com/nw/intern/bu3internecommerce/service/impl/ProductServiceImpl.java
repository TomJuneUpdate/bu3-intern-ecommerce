package com.nw.intern.bu3internecommerce.service.impl;

import com.nw.intern.bu3internecommerce.dto.ProductDto;
import com.nw.intern.bu3internecommerce.dto.request.AddProductRequest;
import com.nw.intern.bu3internecommerce.dto.response.ApiResponse;
import com.nw.intern.bu3internecommerce.entity.Category;
import com.nw.intern.bu3internecommerce.entity.Product;
import com.nw.intern.bu3internecommerce.exception.ResourceNotFoundException;
import com.nw.intern.bu3internecommerce.repository.CategoryRepository;
import com.nw.intern.bu3internecommerce.repository.ProductRepository;
import com.nw.intern.bu3internecommerce.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public ApiResponse<ProductDto> getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        return ApiResponse.ok(convertToDto(product));
    }

    @Override
    public ApiResponse<ProductDto> createProduct(AddProductRequest request) {
        // 🔹 1. Kiểm tra danh mục, nếu chưa có thì tạo mới
        Category category = categoryRepository.findByName(request.getCategory())
                .orElseGet(() -> {
                    Category newCategory = new Category();
                    newCategory.setName(request.getCategory());
                    newCategory.setCode(generateCategoryCode());
                    return categoryRepository.save(newCategory);
                });

        // 🔹 2. Tạo mã sản phẩm mới theo format {category_code}_xxxxxx
        String productCode = generateProductCode(category);

        // 🔹 3. Tạo sản phẩm
        Product product = new Product();
        product.setCode(productCode);
        product.setName(request.getName());
        product.setMrpPrice(request.getMrpPrice());
        product.setSellingPrice(request.getSellingPrice());
        product.setQuantity(request.getQuantity());
        product.setDiscountPercentage(request.getDiscountPercentage());
        product.setDescription(request.getDescription());
        product.setColor(request.getColor());
        product.setImageUrls(request.getImageUrls());
        product.setSizes(request.getSizes());
        product.setCategory(category);

        // 🔹 4. Lưu vào DB
        Product savedProduct = productRepository.save(product);

        return ApiResponse.ok(convertToDto(savedProduct));
    }


    @Override
    public ApiResponse<ProductDto> updateProduct(Long id, ProductDto productDto) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        Category category = existingProduct.getCategory();
        if (!existingProduct.getCategory().getName().equals(productDto.getCategory().getName())) {
            category = categoryRepository.findByName(productDto.getCategory().getName())
                    .orElseGet(() -> {
                        Category newCategory = new Category();
                        newCategory.setName(productDto.getCategory().getName());
                        newCategory.setCode(generateCategoryCode());
                        return categoryRepository.save(newCategory);
                    });
        }

        existingProduct.setName(productDto.getName());
        existingProduct.setMrpPrice(productDto.getMrpPrice());
        existingProduct.setSellingPrice(productDto.getSellingPrice());
        existingProduct.setQuantity(productDto.getQuantity());
        existingProduct.setDiscountPercentage(productDto.getDiscountPercentage());
        existingProduct.setDescription(productDto.getDescription());
        existingProduct.setColor(productDto.getColor());
        existingProduct.setImageUrls(productDto.getImageUrls());
        existingProduct.setSizes(productDto.getSizes());
        existingProduct.setCategory(category);

        Product updatedProduct = productRepository.save(existingProduct);

        return ApiResponse.ok(convertToDto(updatedProduct));
    }


    @Override
    public ApiResponse<Void> deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            return ApiResponse.fail("Product not found");
        }
        productRepository.deleteById(id);
        return ApiResponse.ok();
    }
    public Page<ProductDto> getAllProducts(int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Product> products = productRepository.findAll(pageable);
        return products.map(this::convertToDto);
    }

    /**
     * Chuyển đổi từ Product sang ProductDto
     */
    @Override
    public ProductDto convertToDto(Product product) {
        return ProductDto.builder()
                .code(product.getCode())
                .name(product.getName())
                .mrpPrice(product.getMrpPrice())
                .sellingPrice(product.getSellingPrice())
                .quantity(product.getQuantity())
                .discountPercentage(product.getDiscountPercentage())
                .description(product.getDescription())
                .color(product.getColor())
                .imageUrls(product.getImageUrls())
                .sizes(product.getSizes())
                .category(product.getCategory())
                .build();
    }

    /**
     * Tạo mã sản phẩm theo định dạng {category_code}_xxxxxx
     */
    private String generateProductCode(Category category) {
        String maxProductCode = productRepository.findMaxProductCodeByCategory(category.getCode());
        int nextNumber;
        if (maxProductCode == null) {
            nextNumber = 1;
        } else {
            String numberPart = maxProductCode.substring(maxProductCode.lastIndexOf("_") + 1);
            nextNumber = Integer.parseInt(numberPart) + 1;
        }
        if (nextNumber > 999999) {
            throw new IllegalStateException("Số lượng sản phẩm trong danh mục " + category.getCode() + " đã đạt giới hạn!");
        }
        return String.format("%s_%06d", category.getCode(), nextNumber);
    }

    /**
     * Sinh mã danh mục tự động (0001 - 9999)
     */
    private String generateCategoryCode() {
        String maxCode = categoryRepository.findMaxCategoryCode();

        if (maxCode == null) {
            return "0001";
        }
        int nextCode = Integer.parseInt(maxCode) + 1;
        return String.format("%04d", nextCode);
    }
}
