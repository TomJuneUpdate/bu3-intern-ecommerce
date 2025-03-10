package com.nw.intern.bu3internecommerce.repository;

import com.nw.intern.bu3internecommerce.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByName(String name);

    @Query(value = "SELECT MAX(c.code) FROM categorys c", nativeQuery = true)
    String findMaxCategoryCode();

    boolean existsByName(String name);
}
