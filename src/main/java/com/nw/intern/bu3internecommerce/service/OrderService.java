package com.nw.intern.bu3internecommerce.service;

import com.nw.intern.bu3internecommerce.dto.OrderDto;
import com.nw.intern.bu3internecommerce.entity.order.OrderStatus;
import org.springframework.data.domain.Page;

/**
 * Service xử lý các logic liên quan đến đơn hàng
 */
public interface OrderService {
    /**
     * Đặt đơn hàng mới
     * @param userId ID của người dùng
     * @param addressId ID của địa chỉ giao hàng
     * @return Thông tin đơn hàng đã đặt
     */
    OrderDto placeOrder(Long userId, Long addressId);

    /**
     * Thay đổi trạng thái đơn hàng
     * @param orderId ID của đơn hàng
     * @param newStatus Trạng thái mới
     * @return Thông tin đơn hàng đã cập nhật
     */
    OrderDto changeOrderStatus(Long orderId, OrderStatus newStatus);

    /**
     * Lấy danh sách đơn hàng của người dùng
     * @param userId ID của người dùng
     * @param page Số trang
     * @param size Số lượng phần tử mỗi trang
     * @return Danh sách đơn hàng đã phân trang
     */
    Page<OrderDto> getUserOrders(Long userId, int page, int size);

    /**
     * Lấy chi tiết đơn hàng
     * @param orderId ID của đơn hàng
     * @return Thông tin chi tiết đơn hàng
     */
    OrderDto getOrderDetails(Long orderId);

    /**
     * Xử lý thanh toán đơn hàng
     * @param orderId ID của đơn hàng
     * @param paymentMethod Phương thức thanh toán
     * @return Thông tin đơn hàng đã thanh toán
     */
    OrderDto processPayment(Long orderId, String paymentMethod);
}
