package com.nw.intern.bu3internecommerce.controller;

import com.nw.intern.bu3internecommerce.dto.OrderDto;
import com.nw.intern.bu3internecommerce.dto.response.ApiResponse;
import com.nw.intern.bu3internecommerce.entity.order.OrderStatus;
import com.nw.intern.bu3internecommerce.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller quản lý các API liên quan đến đơn hàng
 */
@RestController
@RequestMapping("${api.prefix}")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    /**
     * Đặt đơn hàng mới
     *
     * @param userId    ID của người dùng
     * @param addressId ID của địa chỉ giao hàng
     * @return Thông tin đơn hàng đã đặt
     */
    @PostMapping("/{userId}/place")
    public ApiResponse<OrderDto> placeOrder(
            @PathVariable Long userId,
            @RequestParam Long addressId) {
        return ApiResponse.ok(orderService.placeOrder(userId, addressId));
    }

    /**
     * Thay đổi trạng thái đơn hàng
     *
     * @param orderId   ID của đơn hàng
     * @param newStatus Trạng thái mới
     * @return Thông tin đơn hàng đã cập nhật
     */
    @PutMapping("/{orderId}/status")
    public ApiResponse<OrderDto> changeOrderStatus(
            @PathVariable Long orderId,
            @RequestParam OrderStatus newStatus) {
        return ApiResponse.ok(orderService.changeOrderStatus(orderId, newStatus));
    }

    /**
     * Lấy danh sách đơn hàng của người dùng
     *
     * @param userId ID của người dùng
     * @param page   Số trang (mặc định: 0)
     * @param size   Số lượng phần tử mỗi trang (mặc định: 10)
     * @return Danh sách đơn hàng đã phân trang
     */
    @GetMapping("/{userId}/orders")
    public ApiResponse<Page<OrderDto>> getUserOrders(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.ok(orderService.getUserOrders(userId, page, size));
    }

    /**
     * Lấy chi tiết đơn hàng
     *
     * @param orderId ID của đơn hàng
     * @return Thông tin chi tiết đơn hàng
     */
    @GetMapping("/{orderId}")
    public ApiResponse<OrderDto> getOrderDetails(@PathVariable Long orderId) {
        return ApiResponse.ok(orderService.getOrderDetails(orderId));
    }

    /**
     * Xử lý thanh toán đơn hàng
     *
     * @param orderId       ID của đơn hàng
     * @param paymentMethod Phương thức thanh toán
     * @return Thông tin đơn hàng đã thanh toán
     */
    @PostMapping("/{orderId}/payment")
    public ApiResponse<OrderDto> processPayment(
            @PathVariable Long orderId,
            @RequestParam String paymentMethod) {
        return ApiResponse.ok(orderService.processPayment(orderId, paymentMethod));
    }
}
