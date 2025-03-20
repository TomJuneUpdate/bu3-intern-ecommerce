package com.nw.intern.bu3internecommerce.entity.order;

/**
 * Enum định nghĩa các trạng thái của đơn hàng
 */
public enum OrderStatus {
    PENDING,    // Chờ xử lý
    PAID,       // Đã thanh toán
    PROCESSING, // Đang xử lý
    SHIPPED,    // Đã giao hàng
    DELIVERED,  // Đã nhận hàng
    CANCELLED,  // Đã hủy
    REFUNDED    // Đã hoàn tiền
}
