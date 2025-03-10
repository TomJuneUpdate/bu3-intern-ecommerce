package com.nw.intern.bu3internecommerce.repository;

import com.nw.intern.bu3internecommerce.entity.PaymentOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentOrderRepo extends JpaRepository<PaymentOrder, Long> {
}
