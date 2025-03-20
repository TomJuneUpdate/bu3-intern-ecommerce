package com.nw.intern.bu3internecommerce.controller;

import com.nw.intern.bu3internecommerce.dto.UserDto;
import com.nw.intern.bu3internecommerce.dto.response.ApiResponse;
import com.nw.intern.bu3internecommerce.entity.user.Address;
import com.nw.intern.bu3internecommerce.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

/**
 * Controller quản lý các API liên quan đến người dùng
 */
@RestController
@RequestMapping("api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    /**
     * Lấy danh sách tất cả người dùng có phân trang
     * @param page Số trang (mặc định: 0)
     * @param size Số lượng phần tử mỗi trang (mặc định: 10)
     * @param sortBy Tiêu chí sắp xếp (mặc định: id)
     * @param sortDir Thứ tự sắp xếp (mặc định: tăng dần)
     * @return Danh sách người dùng đã phân trang
     */
    @GetMapping
    public ApiResponse<Page<UserDto>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return ApiResponse.ok(userService.getAllUsers(page, size, sortBy, sortDir));
    }

    /**
     * Lấy thông tin người dùng theo ID
     * @param userId ID của người dùng
     * @return Thông tin người dùng
     */
    @GetMapping("/{userId}")
    public ApiResponse<UserDto> getUserById(@PathVariable Long userId) {
        return ApiResponse.ok(userService.getUserById(userId));
    }

    /**
     * Lấy thông tin người dùng đang đăng nhập
     * @param userDetails Thông tin người dùng từ Spring Security
     * @return Thông tin người dùng hiện tại
     */
    @GetMapping("/me")
    public ApiResponse<UserDto> getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        return ApiResponse.ok(userService.getCurrentUser(userDetails.getUsername()));
    }

    /**
     * Cập nhật thông tin người dùng
     * @param userId ID của người dùng cần cập nhật
     * @param userDto Thông tin mới của người dùng
     * @return Thông tin người dùng đã được cập nhật
     */
    @PutMapping("/{userId}")
    public ApiResponse<UserDto> updateUser(
            @PathVariable Long userId,
            @RequestBody UserDto userDto) {
        UserDto updatedUser = userService.updateUserById(userId, userDto);
        return ApiResponse.ok(updatedUser);
    }

    /**
     * Xóa người dùng (soft delete)
     * @param id ID của người dùng cần xóa
     * @return Thông báo xóa thành công
     */
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUserById(id, false);
        return ApiResponse.ok();
    }

    /**
     * Thêm địa chỉ mới cho người dùng
     * @param userId ID của người dùng
     * @param address Thông tin địa chỉ mới
     * @return Địa chỉ đã được thêm
     */
    @PostMapping("/{userId}/addresses")
    public ApiResponse<Address> addAddress(@PathVariable Long userId, @RequestBody Address address) {
        return ApiResponse.ok(userService.addAddress(userId, address));
    }

    /**
     * Cập nhật địa chỉ của người dùng
     * @param userId ID của người dùng
     * @param addressId ID của địa chỉ cần cập nhật
     * @param address Thông tin địa chỉ mới
     * @return Địa chỉ đã được cập nhật
     */
    @PutMapping("/{userId}/addresses/{addressId}")
    public ApiResponse<Address> updateAddress(
            @PathVariable Long userId,
            @PathVariable Long addressId,
            @RequestBody Address address) {
        return ApiResponse.ok(userService.updateAddress(userId, addressId, address));
    }

    /**
     * Xóa địa chỉ của người dùng
     * @param userId ID của người dùng
     * @param addressId ID của địa chỉ cần xóa
     * @return Thông báo xóa thành công
     */
    @DeleteMapping("/{userId}/addresses/{addressId}")
    public ApiResponse<Void> deleteAddress(@PathVariable Long userId, @PathVariable Long addressId) {
        userService.deleteAddress(userId, addressId);
        return ApiResponse.ok();
    }

    /**
     * Lấy danh sách địa chỉ của người dùng có phân trang
     * @param userId ID của người dùng
     * @param page Số trang (mặc định: 0)
     * @param size Số lượng phần tử mỗi trang (mặc định: 10)
     * @return Danh sách địa chỉ đã phân trang
     */
    @GetMapping("/{userId}/addresses")
    public ApiResponse<Page<Address>> getUserAddresses(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.ok(userService.getUserAddresses(userId, page, size));
    }
}
