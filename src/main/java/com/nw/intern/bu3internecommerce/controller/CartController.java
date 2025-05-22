package com.nw.intern.bu3internecommerce.controller;

import com.nw.intern.bu3internecommerce.dto.CartDto;
import com.nw.intern.bu3internecommerce.dto.request.AddToCartRequest;
import com.nw.intern.bu3internecommerce.dto.response.ApiResponse;
import com.nw.intern.bu3internecommerce.service.CartService;
import com.nw.intern.bu3internecommerce.service.UserService;
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
@RequestMapping("${api.prefix}/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;
    private final UserService userService;

    @GetMapping
    public ApiResponse<CartDto> getCartByUser() {
        CartDto cart = cartService.getCartByUser();
        return ApiResponse.ok(cart);
    }

    @PostMapping("/add")
    public ApiResponse<CartDto> addToCart(
            @RequestBody AddToCartRequest request
    ) {
        Long userId = userService.getAuthenticatedUser().getId();
        CartDto updatedCart = cartService.addToCart(userId, request);
        return ApiResponse.ok(updatedCart);
    }

    @PutMapping("/update/{cartItemId}")
    public ApiResponse<CartDto> updateCartItem(
            @PathVariable Long cartItemId,
            @RequestParam int quantity
    ) {
        Long userId = userService.getAuthenticatedUser().getId();
        CartDto updatedCart = cartService.updateCartItem(userId, cartItemId, quantity);
        return ApiResponse.ok(updatedCart);
    }


    @DeleteMapping("/remove/{cartItemId}")
    public ApiResponse<Void> removeCartItem(
            @PathVariable Long cartItemId
    ) {
        Long userId = userService.getAuthenticatedUser().getId();
        cartService.removeCartItem(userId, cartItemId);
        return ApiResponse.ok();
    }

    @DeleteMapping("/clear")
    public ApiResponse<Void> clearCart() {
        Long userId = userService.getAuthenticatedUser().getId();
        cartService.clearCart(userId);
        return ApiResponse.ok();
    }
}
