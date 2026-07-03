package com.fmi.springcourse.marketplace.cart.dto;

import com.fmi.springcourse.marketplace.cart.entity.CartItem;

import java.math.BigDecimal;

public record CartItemResponse(
        Long id,
        Long productId,
        String productName,
        BigDecimal price,
        Integer quantity,
        String mainImage,
        Integer maxQuantity
) {
    public CartItemResponse(CartItem item) {
        this(
                item.getId(),
                item.getProduct().getId(),
                item.getProduct().getName(),
                item.getProduct().getPrice(),
                item.getQuantity(),
                item.getProduct().getMainImage(),
                item.getProduct().getQuantity()
        );
    }
}
