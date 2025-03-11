package com.nw.intern.bu3internecommerce.dto;

import com.nw.intern.bu3internecommerce.entity.Product;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CartItemDto {

    private Product product;

    private String size;

    private String color;

    private Integer purchaseQuantity = 1;
}
