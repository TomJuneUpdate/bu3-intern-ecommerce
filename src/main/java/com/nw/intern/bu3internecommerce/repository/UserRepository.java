package com.nw.intern.bu3internecommerce.repository;

import com.nw.intern.bu3internecommerce.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}
