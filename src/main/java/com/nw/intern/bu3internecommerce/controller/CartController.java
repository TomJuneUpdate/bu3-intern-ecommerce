package com.nw.intern.bu3internecommerce.controller;

import com.nw.intern.bu3internecommerce.dto.CartDto;
import com.nw.intern.bu3internecommerce.dto.request.AddToCartRequest;
import com.nw.intern.bu3internecommerce.dto.response.ApiResponse;
import com.nw.intern.bu3internecommerce.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @GetMapping
    public ApiResponse<CartDto> getCartByUser() {
        CartDto cart = cartService.getCartByUser();
        return ApiResponse.ok(cart);
    }

    @PostMapping("/{userId}/add")
    public ApiResponse<CartDto> addToCart(
            @PathVariable Long userId,
            @RequestBody AddToCartRequest request
    ) {
        CartDto updatedCart = cartService.addToCart(userId, request);
        return ApiResponse.ok(updatedCart);
    }

    @PutMapping("/{userId}/update/{cartItemId}")
    public ApiResponse<CartDto> updateCartItem(
            @PathVariable Long userId,
            @PathVariable Long cartItemId,
            @RequestParam int quantity
    ) {
        CartDto updatedCart = cartService.updateCartItem(userId, cartItemId, quantity);
        return ApiResponse.ok(updatedCart);
    }


    @DeleteMapping("/{userId}/remove/{cartItemId}")
    public ApiResponse<Void> removeCartItem(
            @PathVariable Long userId,
            @PathVariable Long cartItemId
    ) {
        cartService.removeCartItem(userId, cartItemId);
        return ApiResponse.ok();
    }

    @DeleteMapping("/{userId}/clear")
    public ApiResponse<Void> clearCart(@PathVariable Long userId) {
        cartService.clearCart(userId);
        return ApiResponse.ok();
    }
}
