package com.nw.intern.bu3internecommerce.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProductRequest {
    private String name;
    private double price;
    private String category;
    private int stock;
}
