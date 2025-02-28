package com.nw.intern.bu3internecommerce.repository;

import com.nw.intern.bu3internecommerce.model.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
