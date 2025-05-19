package com.nw.intern.bu3internecommerce.repository;

import com.nw.intern.bu3internecommerce.entity.user.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);
}
