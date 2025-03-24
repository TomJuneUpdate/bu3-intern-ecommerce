package com.nw.intern.bu3internecommerce.repository;

import com.nw.intern.bu3internecommerce.entity.user.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Long> {
    List<Address> findByUser_Id(Long userId);
}
