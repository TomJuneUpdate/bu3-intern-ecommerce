package com.nw.intern.bu3internecommerce.service.impl;

import com.nw.intern.bu3internecommerce.dto.UserDto;
import com.nw.intern.bu3internecommerce.entity.user.Address;
import com.nw.intern.bu3internecommerce.entity.user.User;
import com.nw.intern.bu3internecommerce.exception.ResourceNotFoundException;
import com.nw.intern.bu3internecommerce.repository.AddressRepository;
import com.nw.intern.bu3internecommerce.repository.UserRepository;
import com.nw.intern.bu3internecommerce.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service xử lý các logic liên quan đến người dùng
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;

    @Override
    public Page<UserDto> getAllUsers(int page, int size, String sortBy, String sortDir) {
        // Tạo đối tượng sắp xếp dựa trên tham số
        Sort sort = sortDir.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<User> users = userRepository.findAll(pageable);
        return users.map(this::convertToDto);
    }

    @Override
    public UserDto getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng với ID: " + userId));
        return convertToDto(user);
    }


    @Override
    public UserDto updateUserById(Long userId, UserDto userDto) {
        // Tìm người dùng cần cập nhật
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng"));

        // Cập nhật thông tin mới
        existingUser.setFirstName(userDto.getFirstName());
        existingUser.setLastName(userDto.getLastName());
        existingUser.setEmail(userDto.getEmail());
        existingUser.setPhone(userDto.getPhone());
        existingUser.setDateOfBirth(userDto.getDateOfBirth());

        // Lưu thông tin đã cập nhật
        User updatedUser = userRepository.save(existingUser);
        return convertToDto(updatedUser);
    }

    @Override
    public void deleteUserById(Long id, Boolean isActive) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng với ID: " + id));
        user.setActive(isActive);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public Address addAddress(Long userId, Address address) {
        // Tìm người dùng cần thêm địa chỉ và load addresses
        User user = userRepository.findByIdWithAddresses(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng với ID: " + userId));
        
        // Set user cho address
        address.setUser(user);
        
        // Thêm address vào collection của user
        user.getAddresses().add(address);
        
        // Lưu user để cập nhật collection
        userRepository.save(user);
        
        // Lưu và trả về address
        return addressRepository.save(address);
    }

    @Override
    @Transactional
    public Address updateAddress(Long userId, Long addressId, Address updatedAddress) {
        // Tìm địa chỉ cần cập nhật và load user
        Address existingAddress = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy địa chỉ với ID: " + addressId));

        // Kiểm tra xem địa chỉ có thuộc về người dùng không
        if (!existingAddress.getUser().getId().equals(userId)) {
            throw new ResourceNotFoundException("Không tìm thấy địa chỉ cho người dùng với ID: " + userId);
        }

        // Cập nhật thông tin địa chỉ
        existingAddress.setCity(updatedAddress.getCity());
        existingAddress.setState(updatedAddress.getState());
        existingAddress.setPostalCode(updatedAddress.getPostalCode());
        existingAddress.setFullName(updatedAddress.getFullName());
        existingAddress.setPhone(updatedAddress.getPhone());
        existingAddress.setAddressNote(updatedAddress.getAddressNote());

        return addressRepository.save(existingAddress);
    }

    @Override
    @Transactional
    public void deleteAddress(Long userId, Long addressId) {
        // Tìm người dùng và load addresses
        User user = userRepository.findByIdWithAddresses(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng với ID: " + userId));
        
        // Tìm địa chỉ cần xóa
        Address address = user.getAddresses().stream()
                .filter(a -> a.getId().equals(addressId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy địa chỉ với ID: " + addressId));

        // Xóa address khỏi collection của user
        user.getAddresses().remove(address);
        userRepository.save(user);
        
        // Xóa address
        addressRepository.delete(address);
    }

    @Override
    public Page<Address> getUserAddresses(Long userId, int page, int size) {
        // Tìm người dùng
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng với ID: " + userId));

        List<Address> addresses = addressRepository.findByUser_Id(userId);

        int start = page * size;
        int end = Math.min(start + size, addresses.size());

        if (start > addresses.size()) {
            return new PageImpl<>(new ArrayList<>(), PageRequest.of(page, size), addresses.size());
        }

        return new PageImpl<>(addresses.subList(start, end), PageRequest.of(page, size), addresses.size());
    }

    /**
     * Chuyển đổi từ Entity User sang DTO
     *
     * @param user Entity User cần chuyển đổi
     * @return UserDto đã được chuyển đổi
     */
    @Override
    public UserDto convertToDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .isActive(user.isActive())
                .lastName(user.getLastName())
                .phone(user.getPhone() != null ? user.getPhone() : null)
                .dateOfBirth(user.getDateOfBirth())
                .build();
    }

    @Override
    public User getAuthenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth instanceof AnonymousAuthenticationToken) {
            throw new EntityNotFoundException("Log in required!");
        }
        String username = auth.getName();
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }
}
