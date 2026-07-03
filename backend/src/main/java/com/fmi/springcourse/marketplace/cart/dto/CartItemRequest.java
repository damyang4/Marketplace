package com.fmi.springcourse.marketplace.cart.dto;

import jakarta.validation.constraints.Positive;

public record CartItemRequest(
        @Positive(message = "Quantity must be a positive number")
        Integer requestedQuantity) {
}
