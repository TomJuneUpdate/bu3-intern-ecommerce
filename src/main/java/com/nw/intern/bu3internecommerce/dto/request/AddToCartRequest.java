package com.nw.intern.bu3internecommerce.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddToCartRequest {
    private Long productId;
    private String size;
    private String color;
    private int quantity;
}
