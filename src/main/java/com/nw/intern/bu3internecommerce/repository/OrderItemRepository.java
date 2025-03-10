package com.nw.intern.bu3internecommerce.repository;

import com.nw.intern.bu3internecommerce.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
