package com.nw.intern.bu3internecommerce.dto;

import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProductDto {
    private String name;
    private Double price;
    private String category;
    private Integer stock;
    private String description;
    private String imageUrl;
}
