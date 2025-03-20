package com.nw.intern.bu3internecommerce.repository;

import com.nw.intern.bu3internecommerce.entity.cart.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUserId(Long userId);
    Optional<Cart> findByUserUsername(String name);
}
