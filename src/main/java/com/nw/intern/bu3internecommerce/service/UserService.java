package com.nw.intern.bu3internecommerce.service;

import com.nw.intern.bu3internecommerce.dto.UserDto;
import com.nw.intern.bu3internecommerce.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    Page<UserDto> getAllUsers(int page, int size, String sortBy, String sortDir);

    UserDto updateUserById(Long userId, UserDto userDto);

    void deleteUserById(Long id, Boolean isActive);

    UserDto convertToDto(User user);
}
