package com.nw.intern.bu3internecommerce.service.impl;

import com.nw.intern.bu3internecommerce.dto.CartDto;
import com.nw.intern.bu3internecommerce.dto.CartItemDto;
import com.nw.intern.bu3internecommerce.dto.request.AddToCartRequest;
import com.nw.intern.bu3internecommerce.entity.cart.Cart;
import com.nw.intern.bu3internecommerce.entity.cart.CartItem;
import com.nw.intern.bu3internecommerce.entity.Product;
import com.nw.intern.bu3internecommerce.exception.ResourceNotFoundException;
import com.nw.intern.bu3internecommerce.repository.CartItemRepository;
import com.nw.intern.bu3internecommerce.repository.CartRepository;
import com.nw.intern.bu3internecommerce.repository.ProductRepository;
import com.nw.intern.bu3internecommerce.repository.UserRepository;
import com.nw.intern.bu3internecommerce.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;

    @Override
    public CartDto getCartByUserId(Long userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(()
                        -> new ResourceNotFoundException("Cart not found for user: " + userId));
        return convertToCartDto(cart);
    }

    @Override
    public CartDto addToCart(Long userId, AddToCartRequest request) {
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        Cart cart = cartRepository.findByUserId(userId).orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setUser(userRepository.findById(userId).get());
            return cartRepository.save(newCart);
        });

        CartItem cartItem = cart.getCartItems().stream()
                .filter(item -> item.getProduct().getId().equals(product.getId()) &&
                        item.getSize().equals(request.getSize()) &&
                        item.getColor().equals(request.getColor()))
                .findFirst()
                .orElse(null);

        if (cartItem == null) {
            cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setSize(request.getSize());
            cartItem.setColor(request.getColor());
            cartItem.setPurchaseQuantity(request.getQuantity());
            cart.getCartItems().add(cartItem);
        } else {
            cartItem.setPurchaseQuantity(cartItem.getPurchaseQuantity() + request.getQuantity());
        }

        cartRepository.save(cart);
        return convertToCartDto(cart);
    }

    @Override
    public CartDto updateCartItem(Long userId, Long cartItemId, int quantity) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found"));

        if (!cartItem.getCart().getUser().getId().equals(userId)) {
            throw new IllegalStateException("User does not own this cart item");
        }

        if (quantity <= 0) {
            cartItemRepository.delete(cartItem); // Nếu số lượng <= 0, xóa sản phẩm khỏi giỏ hàng
        } else {
            cartItem.setPurchaseQuantity(quantity);
            cartItemRepository.save(cartItem);
        }

        return convertToCartDto(cartItem.getCart());
    }

    @Override
    public void removeCartItem(Long userId, Long cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found"));

        if (!cartItem.getCart().getUser().getId().equals(userId)) {
            throw new IllegalStateException("User does not own this cart item");
        }

        cartItemRepository.delete(cartItem);
    }

    @Override
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
                .cartItems(cart.getCartItems().stream()
                        .map(this::convertToCartItemDto)
                        .collect(Collectors.toSet()))
                .totalSellingPrice(cart.getTotalSellingPrice())
                .totalMrpPrice(cart.getTotalMrpPrice())
                .discount(cart.getDiscount())
                .couponCode(cart.getCouponCode())
                .build();
    }

    private CartItemDto convertToCartItemDto(CartItem cartItem) {
        return CartItemDto.builder()
                .product(cartItem.getProduct())
                .size(cartItem.getSize())
                .color(cartItem.getColor())
                .purchaseQuantity(cartItem.getPurchaseQuantity())
                .build();
    }
}
