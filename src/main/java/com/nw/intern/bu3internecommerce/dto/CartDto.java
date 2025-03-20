package com.nw.intern.bu3internecommerce.dto;

import com.nw.intern.bu3internecommerce.entity.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Set;

@Getter
@Setter
@Builder
public class CartDto {
    private Long id;
    private User user;
    private Set<CartItemDto> cartItems;
    private Double totalSellingPrice;
    private String couponCode;
}
