package com.nw.intern.bu3internecommerce.dto;

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
}
