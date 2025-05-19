package com.nw.intern.bu3internecommerce.service;

import com.nw.intern.bu3internecommerce.dto.UserDto;
import com.nw.intern.bu3internecommerce.entity.user.Address;
import com.nw.intern.bu3internecommerce.entity.user.User;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import jakarta.validation.constraints.Min;
import java.math.BigDecimal;

@Service
public interface UserService {
    Page<UserDto> getAllUsers(int page, int size, String sortBy, String sortDir);

    UserDto getUserById(Long userId);


    UserDto updateUserById(Long userId, UserDto userDto);

    void deleteUserById(Long id, Boolean isActive);

    UserDto convertToDto(User user);

    Address addAddress(Long userId, Address address);

    Address updateAddress(Long userId, Long addressId, Address address);

    void deleteAddress(Long userId, Long addressId);

    Page<Address> getUserAddresses(Long userId, int page, int size);

    User getAuthenticatedUser();
}
