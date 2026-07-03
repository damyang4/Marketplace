package com.fmi.springcourse.marketplace.order.dto;

import com.fmi.springcourse.marketplace.order.entity.OrderItem;

import java.math.BigDecimal;

public record OrderItemResponse(
        Long productId,
        String productName,
        BigDecimal price,
        Integer quantity
) {
    public OrderItemResponse(OrderItem item) {
        this(
            item.getProduct().getId(),
            item.getProduct().getName(),
            item.getProduct().getPrice(),
            item.getQuantity()
        );
    }
}
