package com.nw.intern.bu3internecommerce.repository;

import com.nw.intern.bu3internecommerce.entity.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductRepository {
    private static ProductRepository instance;
    private List<Product> products;
    public static ProductRepository getInstance() {
        if (instance == null) {
            instance = new ProductRepository();
        }
        return instance;
    }
    private ProductRepository() {
        products = new ArrayList<Product>();
    }

}
