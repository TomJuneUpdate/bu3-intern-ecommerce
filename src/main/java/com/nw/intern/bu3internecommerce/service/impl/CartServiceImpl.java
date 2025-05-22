package com.nw.intern.bu3internecommerce.service.impl;

import com.nw.intern.bu3internecommerce.dto.CartDto;
import com.nw.intern.bu3internecommerce.dto.CartItemDto;
import com.nw.intern.bu3internecommerce.dto.request.AddToCartRequest;
import com.nw.intern.bu3internecommerce.entity.cart.Cart;
import com.nw.intern.bu3internecommerce.entity.cart.CartItem;
import com.nw.intern.bu3internecommerce.entity.Product;
import com.nw.intern.bu3internecommerce.entity.user.User;
import com.nw.intern.bu3internecommerce.exception.ResourceNotFoundException;
import com.nw.intern.bu3internecommerce.repository.CartItemRepository;
import com.nw.intern.bu3internecommerce.repository.CartRepository;
import com.nw.intern.bu3internecommerce.repository.ProductRepository;
import com.nw.intern.bu3internecommerce.repository.UserRepository;
import com.nw.intern.bu3internecommerce.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;

    @Override
    @Transactional
    public CartDto getCartByUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Cart cart = cartRepository.findByUserUsernameWithItems(username)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found for user"));
        return convertToCartDto(cart);
    }

    @Override
    @Transactional
    public CartDto addToCart(Long userId, AddToCartRequest request) {
        // Kiểm tra user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Kiểm tra sản phẩm
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        // Validate selling price
        if (product.getSellingPrice() == null) {
            throw new IllegalStateException("Product selling price is null for product ID: " + product.getId());
        }

        // Validate quantity
        if (request.getQuantity() <= 0) {
            throw new IllegalStateException("Quantity must be greater than 0");
        }

        // Validate stock quantity
        if (product.getQuantity() < request.getQuantity()) {
            throw new IllegalStateException("Not enough stock. Available quantity: " + product.getQuantity());
        }

        // Validate size
        if (!product.getSizes().contains(request.getSize())) {
            throw new IllegalStateException("Invalid size. Available sizes: " + product.getSizes());
        }

        // Validate color
        if (!product.getColor().contains(request.getColor())) {
            throw new IllegalStateException("Invalid color. Available colors: " + product.getColor());
        }

        // Tìm hoặc tạo mới Cart, fetch cartItems ngay trong truy vấn
        Cart cart = cartRepository.findByUserIdWithItems(userId)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    newCart.setCartItems(new HashSet<>());
                    return cartRepository.save(newCart);
                });

        // Tìm CartItem theo productId, size, color
        Optional<CartItem> optionalCartItem = cart.getCartItems().stream()
                .filter(item -> item.getProduct().getId().equals(product.getId()) &&
                        item.getSize().equals(request.getSize()) &&
                        item.getColor().equals(request.getColor()))
                .findFirst();

        CartItem cartItem;
        if (optionalCartItem.isPresent()) {
            // Cập nhật số lượng
            cartItem = optionalCartItem.get();
            int newQuantity = cartItem.getPurchaseQuantity() + request.getQuantity();
            
            // Kiểm tra tổng số lượng sau khi cập nhật
            if (product.getQuantity() < newQuantity) {
                throw new IllegalStateException("Not enough stock. Available quantity: " + product.getQuantity() + 
                    ", Current cart quantity: " + cartItem.getPurchaseQuantity() + 
                    ", Requested additional quantity: " + request.getQuantity());
            }
            
            cartItem.setPurchaseQuantity(newQuantity);
            // Giữ nguyên giá tại thời điểm thêm vào giỏ
            if (cartItem.getSellingPrice() == null) {
                cartItem.setSellingPrice(product.getSellingPrice());
                cartItem.setMrpPrice(product.getMrpPrice());
            }
        } else {
            // Tạo mới CartItem
            cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setSize(request.getSize());
            cartItem.setColor(request.getColor());
            cartItem.setPurchaseQuantity(request.getQuantity());
            cartItem.setSellingPrice(product.getSellingPrice());
            cartItem.setMrpPrice(product.getMrpPrice());
            cart.getCartItems().add(cartItem);
        }

        // Lưu CartItem và cập nhật Cart
        cartItemRepository.save(cartItem);
        updateCartTotal(cart);
        cartRepository.save(cart);

        // Trả về CartDto
        return convertToCartDto(cart);
    }

    @Override
    @Transactional
    public CartDto updateCartItem(Long userId, Long cartItemId, int quantity) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found"));

        // Fetch cart with items to avoid lazy loading
        Cart cart = cartRepository.findByUserIdWithItems(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

        if (!cart.getId().equals(cartItem.getCart().getId())) {
            throw new IllegalStateException("User does not own this cart item");
        }

        if (quantity <= 0) {
            cartItemRepository.delete(cartItem);
            cart.getCartItems().remove(cartItem);
        } else {
            cartItem.setPurchaseQuantity(quantity);
            cartItemRepository.save(cartItem);
        }

        updateCartTotal(cart);
        cartRepository.save(cart);
        return convertToCartDto(cart);
    }

    @Override
    @Transactional
    public void removeCartItem(Long userId, Long cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found"));

        // Fetch cart with items to avoid lazy loading
        Cart cart = cartRepository.findByUserIdWithItems(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

        if (!cart.getId().equals(cartItem.getCart().getId())) {
            throw new IllegalStateException("User does not own this cart item");
        }

        cartItemRepository.delete(cartItem);
        cart.getCartItems().remove(cartItem);
        updateCartTotal(cart);
        cartRepository.save(cart);
    }

    @Override
    @Transactional
    public void clearCart(Long userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found for user: " + userId));

        cartItemRepository.deleteAll(cart.getCartItems());
        cart.getCartItems().clear();
        cartRepository.save(cart);
    }

    private CartDto convertToCartDto(Cart cart) {
        return CartDto.builder()
                .user(cart.getUser())
                .id(cart.getId())
                .cartItems(cart.getCartItems().stream()
                        .map(this::convertToCartItemDto)
                        .collect(Collectors.toSet()))
                .totalSellingPrice(cart.getTotalSellingPrice())
                .couponCode(cart.getCouponCode())
                .build();
    }

    private CartItemDto convertToCartItemDto(CartItem cartItem) {
        return CartItemDto.builder()
                .id(cartItem.getId())
                .product(cartItem.getProduct())
                .size(cartItem.getSize())
                .color(cartItem.getColor())
                .purchaseQuantity(cartItem.getPurchaseQuantity())
                .build();
    }

    private void updateCartTotal(Cart cart) {
        double total = 0.0;
        for (CartItem item : cart.getCartItems()) {
            if (item.getSellingPrice() == null) {
                // If selling price is null, try to get it from the product
                if (item.getProduct() != null && item.getProduct().getSellingPrice() != null) {
                    item.setSellingPrice(item.getProduct().getSellingPrice());
                } else {
                    throw new IllegalStateException("Selling price is null for cart item ID: " + item.getId());
                }
            }
            total += item.getSellingPrice() * item.getPurchaseQuantity();
        }
        cart.setTotalSellingPrice(total);
    }
}