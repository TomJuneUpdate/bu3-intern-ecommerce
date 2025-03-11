package com.nw.intern.bu3internecommerce.service;

import com.nw.intern.bu3internecommerce.dto.CartDto;
import com.nw.intern.bu3internecommerce.dto.CartItemDto;
import com.nw.intern.bu3internecommerce.dto.request.AddCategoryRequest;
import com.nw.intern.bu3internecommerce.dto.request.AddToCartRequest;

public interface CartService {
    CartDto getCartByUserId(Long userId);
    CartDto addToCart(Long userId, AddToCartRequest request);
    CartDto updateCartItem(Long userId, Long cartItemId, int quantity);
    void removeCartItem(Long userId, Long cartItemId);
    void clearCart(Long userId);
}
