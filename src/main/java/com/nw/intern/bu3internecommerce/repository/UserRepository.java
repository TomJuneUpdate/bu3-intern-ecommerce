package com.nw.intern.bu3internecommerce.repository;

import com.nw.intern.bu3internecommerce.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);
}
