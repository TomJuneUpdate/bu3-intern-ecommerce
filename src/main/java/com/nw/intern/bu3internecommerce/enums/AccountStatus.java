package com.nw.intern.bu3internecommerce.enums;

/**
 * Enum đại diện cho trạng thái của tài khoản người dùng trong hệ thống.
 * Trạng thái này giúp kiểm soát quyền truy cập và các hành động có thể thực hiện.
 */
public enum AccountStatus {

    /**
     * Tài khoản vừa đăng ký nhưng chưa được xác minh.
     * Thường yêu cầu xác nhận qua email/SMS trước khi kích hoạt.
     */
    PENDING_VERIFICATION,

    /**
     * Tài khoản đã được xác minh và có thể sử dụng đầy đủ chức năng.
     */
    ACTIVE,

    /**
     * Tài khoản bị đình chỉ tạm thời, có thể do vi phạm chính sách hoặc chưa thanh toán.
     * Quản trị viên có thể kích hoạt lại nếu cần.
     */
    SUSPENDED,

    /**
     * Tài khoản bị vô hiệu hóa theo yêu cầu của người dùng.
     * Có thể kích hoạt lại nếu người dùng muốn sử dụng lại tài khoản.
     */
    DEACTIVATED,

    /**
     * Tài khoản bị cấm vĩnh viễn do vi phạm nghiêm trọng.
     * Không thể khôi phục.
     */
    BANNED,

    /**
     * Tài khoản đã bị đóng hoàn toàn (do người dùng yêu cầu hoặc quản trị viên xử lý).
     * Không thể mở lại sau khi đã đóng.
     */
    CLOSED
}
