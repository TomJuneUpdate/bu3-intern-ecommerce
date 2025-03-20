package com.nw.intern.bu3internecommerce.dto;

import com.nw.intern.bu3internecommerce.entity.order.OrderItem;
import com.nw.intern.bu3internecommerce.entity.order.OrderStatus;
import com.nw.intern.bu3internecommerce.entity.user.Address;
import com.nw.intern.bu3internecommerce.entity.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {
    private Long id;

    private String userId;

    private List<OrderItem> orderItems = new ArrayList<>();

    private Address shippingAddress;

    private Double totalPrice;

    private OrderStatus orderStatus;
}
