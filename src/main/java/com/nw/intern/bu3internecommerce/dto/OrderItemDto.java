package com.nw.intern.bu3internecommerce.dto;

import com.nw.intern.bu3internecommerce.entity.Product;
import com.nw.intern.bu3internecommerce.entity.order.Order;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDto {
    private Long id;

    private Order order;

    private Product product;

    private int purchaseQuantity;

    private String size;

    private String color;

    private Double mrpPrice;

    private Double sellingPrice;
}
