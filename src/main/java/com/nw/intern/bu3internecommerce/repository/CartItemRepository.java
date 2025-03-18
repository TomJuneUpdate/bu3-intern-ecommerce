package com.nw.intern.bu3internecommerce.repository;

import com.nw.intern.bu3internecommerce.entity.cart.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    void deleteAllByCartId(Long id);
}
