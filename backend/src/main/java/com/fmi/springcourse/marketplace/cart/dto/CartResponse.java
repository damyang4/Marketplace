package com.fmi.springcourse.marketplace.cart.dto;

import com.fmi.springcourse.marketplace.cart.entity.Cart;

import java.math.BigDecimal;
import java.util.List;

public record CartResponse(Long id,
                           String userProfile,
                           List<CartItemResponse> cartItems,
                           BigDecimal totalAmount,
                           Integer totalItems,
                           String guestToken) {
    public CartResponse(Cart cart) {
        this(
            cart.getId(),
            cart.getUser() != null ? cart.getUser().getProfileName() : "GUEST",
            cart.getCartItems().stream()
                    .map(CartItemResponse::new)
                    .toList(),
            cart.getTotalAmount(),
            cart.getCartItems().stream()
                    .map(CartItemResponse::new)
                    .map(CartItemResponse::quantity)
                    .reduce(0, Integer::sum),
            cart.getGuestToken()
        );
    }
}
