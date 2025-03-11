package com.nw.intern.bu3internecommerce.entity.order;

/**
 * Enum OrderStatus đại diện cho các trạng thái của đơn hàng.
 */
public enum OrderStatus {
    PENDING,   // Đơn hàng vừa được tạo, chưa xác nhận thanh toán
    PLACED,    // Đơn hàng đã được đặt thành công
    CONFIRMED, // Đơn hàng đã được xác nhận bởi hệ thống/admin
    SHIPPED,   // Đơn hàng đã được giao cho đơn vị vận chuyển
    DELIVERED, // Đơn hàng đã được giao thành công đến khách hàng
    CANCELLED  // Đơn hàng bị hủy do khách hàng hoặc hệ thống
}
