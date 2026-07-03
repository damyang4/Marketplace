package com.fmi.springcourse.marketplace.order;

import com.fmi.springcourse.marketplace.order.dto.OrderResponse;
import com.fmi.springcourse.marketplace.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService service;

    @PostMapping("/checkout")
    public ResponseEntity<OrderResponse> checkout(@AuthenticationPrincipal User user) {
        OrderResponse orderResponse = service.createOrderFromCart(user);
        return ResponseEntity.ok(orderResponse);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable Long orderId, @AuthenticationPrincipal User user) {
        // Passing the user ensures a malicious user can't look up random order IDs
        OrderResponse orderResponse = service.getOrderForUser(orderId, user);
        return ResponseEntity.ok(orderResponse);
    }
}