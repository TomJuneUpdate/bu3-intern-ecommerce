package com.nw.intern.bu3internecommerce.repository;

import com.nw.intern.bu3internecommerce.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.addresses WHERE u.id = :userId")
    Optional<User> findByIdWithAddresses(Long userId);
}
