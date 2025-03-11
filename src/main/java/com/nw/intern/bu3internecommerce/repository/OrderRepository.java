package com.nw.intern.bu3internecommerce.repository;

import com.nw.intern.bu3internecommerce.entity.Order;
import com.nw.intern.bu3internecommerce.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser(User user);
}
