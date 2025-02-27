package com.nw.intern.bu3internecommerce.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    private long id;
    private String name;
    private double price;
    private String category;
    private int stock;
}
