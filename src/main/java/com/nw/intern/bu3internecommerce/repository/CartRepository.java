package com.nw.intern.bu3internecommerce.repository;

import com.nw.intern.bu3internecommerce.entity.cart.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUserId(Long userId);
    Optional<Cart> findByUserUsername(String name);

    @Query("SELECT c FROM Cart c LEFT JOIN FETCH c.cartItems WHERE c.user.id = :userId")
    Optional<Cart> findByUserIdWithItems(Long userId);

    @Query("SELECT c FROM Cart c LEFT JOIN FETCH c.cartItems ci LEFT JOIN FETCH ci.product LEFT JOIN FETCH c.user WHERE c.user.username = :username")
    Optional<Cart> findByUserUsernameWithItems(String username);
}
