package com.nw.intern.bu3internecommerce.repository;

import com.nw.intern.bu3internecommerce.entity.Category;
import com.nw.intern.bu3internecommerce.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable);

    @Query("SELECT p.code FROM Product p WHERE p.category.code = :categoryCode ORDER BY p.code DESC LIMIT 1")
    String findMaxProductCodeByCategory(@Param("categoryCode") String categoryCode);
}
