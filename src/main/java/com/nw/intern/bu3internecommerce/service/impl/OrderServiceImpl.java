package com.nw.intern.bu3internecommerce.service.impl;

import com.nw.intern.bu3internecommerce.dto.OrderDto;
import com.nw.intern.bu3internecommerce.dto.OrderItemDto;
import com.nw.intern.bu3internecommerce.entity.cart.Cart;
import com.nw.intern.bu3internecommerce.entity.order.Order;
import com.nw.intern.bu3internecommerce.entity.order.OrderItem;
import com.nw.intern.bu3internecommerce.entity.order.OrderStatus;
import com.nw.intern.bu3internecommerce.entity.user.Address;
import com.nw.intern.bu3internecommerce.entity.user.User;
import com.nw.intern.bu3internecommerce.exception.ResourceNotFoundException;
import com.nw.intern.bu3internecommerce.repository.CartRepository;
import com.nw.intern.bu3internecommerce.repository.OrderRepository;
import com.nw.intern.bu3internecommerce.repository.ProductRepository;
import com.nw.intern.bu3internecommerce.repository.UserRepository;
import com.nw.intern.bu3internecommerce.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service xử lý các logic liên quan đến đơn hàng
 */
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Override
    @Transactional
    public OrderDto placeOrder(Long userId, Long addressId) {
        // Kiểm tra user có tồn tại không
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng"));

        // Kiểm tra giỏ hàng có sản phẩm không
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Giỏ hàng trống"));

        // Tìm địa chỉ từ danh sách của User
        Address selectedAddress = user.getAddresses().stream()
                .filter(address -> address.getId().equals(addressId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy địa chỉ giao hàng"));

        if (cart.getCartItems().isEmpty()) {
            throw new IllegalStateException("Không thể đặt đơn hàng với giỏ hàng trống");
        }

        // Tạo đơn hàng mới
        Order order = new Order();
        order.setUser(user);
        order.setOrderStatus(OrderStatus.PENDING);
        order.setShippingAddress(selectedAddress);
        order = orderRepository.save(order); //Lưu trước để có ID

        // Chuyển sản phẩm từ giỏ hàng sang đơn hàng
        List<OrderItem> orderItems = cart.getCartItems().stream().map(cartItem -> {
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setSize(cartItem.getSize());
            orderItem.setColor(cartItem.getColor());
            orderItem.setMrpPrice(cartItem.getProduct().getMrpPrice().doubleValue());
            orderItem.setSellingPrice(cartItem.getProduct().getSellingPrice().doubleValue());
            orderItem.setPurchaseQuantity(cartItem.getPurchaseQuantity());
            return orderItem;
        }).collect(Collectors.toList());

        // Gán order vào từng orderItem
        Order finalOrder = order;
        orderItems.forEach(orderItem -> orderItem.setOrder(finalOrder));
        order.setOrderItems(orderItems);

        // Tính tổng tiền đơn hàng
        double totalPrice = orderItems.stream()
                .mapToDouble(item -> item.getSellingPrice() * item.getPurchaseQuantity())
                .sum();
        order.setTotalPrice(totalPrice);

        // Lưu đơn hàng và xóa giỏ hàng
        order = orderRepository.save(order);
        cartRepository.delete(cart);

        return convertToOrderDto(order);
    }

    @Override
    @Transactional
    public OrderDto changeOrderStatus(Long orderId, OrderStatus newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy đơn hàng"));

        order.setOrderStatus(newStatus);
        orderRepository.save(order);

        return convertToOrderDto(order);
    }

    @Override
    public Page<OrderDto> getUserOrders(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return orderRepository.findByUserId(userId, pageable)
                .map(this::convertToOrderDto);
    }

    @Override
    public OrderDto getOrderDetails(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy đơn hàng"));
        return convertToOrderDto(order);
    }

    @Override
    @Transactional
    public OrderDto processPayment(Long orderId, String paymentMethod) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy đơn hàng"));

        // Xử lý thanh toán (có thể tích hợp với các cổng thanh toán)
        order.setOrderStatus(OrderStatus.PAID);
        orderRepository.save(order);

        return convertToOrderDto(order);
    }

    /**
     * Chuyển đổi từ Entity Order sang DTO
     * @param order Entity Order cần chuyển đổi
     * @return OrderDto đã được chuyển đổi
     */
    private OrderDto convertToOrderDto(Order order) {
        return OrderDto.builder()
                .id(order.getId())
                .userId(order.getUser().getId().toString())
                .orderItems(order.getOrderItems())
                .shippingAddress(order.getShippingAddress())
                .totalPrice(order.getTotalPrice())
                .orderStatus(order.getOrderStatus())
                .build();
    }

    private OrderItemDto convertOrderItemDto(OrderItem orderItem) {
        return OrderItemDto.builder()
                .id(orderItem.getId())
                .size(orderItem.getSize())
                .color(orderItem.getColor())
                .product(orderItem.getProduct())
                .mrpPrice(orderItem.getMrpPrice())
                .sellingPrice(orderItem.getSellingPrice())
                .purchaseQuantity(orderItem.getPurchaseQuantity())
                .build();
    }
}
