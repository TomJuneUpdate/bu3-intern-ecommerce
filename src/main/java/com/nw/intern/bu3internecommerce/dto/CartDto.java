package com.nw.intern.bu3internecommerce.dto;

import com.nw.intern.bu3internecommerce.entity.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Builder
public class CartDto {
    private User user;
    private Set<CartItemDto> cartItems;
    private double totalSellingPrice;
    private int totalMrpPrice;
    private int discount;
    private String couponCode;
}
