package com.fmi.springcourse.marketplace.payment;

import com.fmi.springcourse.marketplace.user.entity.User;
import com.stripe.exception.StripeException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/payment")
@CrossOrigin(origins = "http://localhost:4200") // Allow your Angular app to talk to Spring
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping("/create-intent")
    public ResponseEntity<Map<String, String>> checkout(@RequestBody Map<String, Object> data,
                                                        @AuthenticationPrincipal User user) {
        try {
            long amount = Long.parseLong(data.get("amount").toString());
            String currency = data.get("currency").toString();

            String sessionUrl = paymentService.createCheckoutSession(amount, currency, user);

            return ResponseEntity.ok(Map.of("sessionUrl", sessionUrl));
        } catch (StripeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
