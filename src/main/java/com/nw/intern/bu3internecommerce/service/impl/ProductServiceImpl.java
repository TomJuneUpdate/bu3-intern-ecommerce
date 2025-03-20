package com.nw.intern.bu3internecommerce.service.impl;

import com.nw.intern.bu3internecommerce.dto.ProductDto;
import com.nw.intern.bu3internecommerce.dto.request.AddProductRequest;
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
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public ProductDto getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        return convertToDto(product);
    }

    @Override
    public ProductDto createProduct(AddProductRequest request) {

        Category category = categoryRepository
                .findByName(request.getCategory())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy danh muc"));

        double sellingPrice = sellingPrice(request.getMrpPrice(), request.getDiscountPercentage());

        String code = generateProductCode(category);

        Product product = Product.builder()
                .code(code)
                .name(request.getName())
                .mrpPrice(request.getMrpPrice())
                .sellingPrice(sellingPrice)
                .quantity(request.getQuantity())
                .discountPercentage(request.getDiscountPercentage())
                .description(request.getDescription())
                .color(request.getColor())
                .imageUrls(request.getImageUrls())
                .sizes(request.getSizes())
                .category(category)
                .build();

        Product savedProduct = productRepository.save(product);

        return convertToDto(savedProduct);
    }

    @Override
    public ProductDto updateProduct(Long id, ProductDto productDto) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        double sellingPrice = sellingPrice(productDto.getMrpPrice()
                , productDto.getDiscountPercentage());
        existingProduct.setName(productDto.getName());
        existingProduct.setMrpPrice(productDto.getMrpPrice());
        existingProduct.setSellingPrice(sellingPrice);
        existingProduct.setQuantity(productDto.getQuantity());
        existingProduct.setDiscountPercentage(productDto.getDiscountPercentage());
        existingProduct.setDescription(productDto.getDescription());
        existingProduct.setColor(productDto.getColor());
        existingProduct.setImageUrls(productDto.getImageUrls());
        existingProduct.setSizes(productDto.getSizes());
        Product updatedProduct = productRepository.save(existingProduct);
        return convertToDto(updatedProduct);
    }



    @Override
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Product not found");
        }
        productRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
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
                .id(product.getId())
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

    private double sellingPrice(double price, int discountPercentage) {
        double sellingPrice = price * (1 - discountPercentage / 100.0);
        return sellingPrice;
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

}
