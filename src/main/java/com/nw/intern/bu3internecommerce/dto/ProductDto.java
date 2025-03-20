package com.nw.intern.bu3internecommerce.dto;

import com.nw.intern.bu3internecommerce.entity.Category;
import com.nw.intern.bu3internecommerce.entity.Review;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Getter
@Builder
public class ProductDto {
    private Long id;
    private String code;
    private String name;
    private Integer quantity;
    private Double mrpPrice;
    private Double sellingPrice;
    private int discountPercentage;
    private Category category;
    private String description;
    private Set<String> color;
    private List<String> imageUrls = new ArrayList<String>();
    private Set<String> sizes;
    private List<Review> reviews = new ArrayList<>();
}
