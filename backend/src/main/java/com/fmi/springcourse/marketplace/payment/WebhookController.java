package com.fmi.springcourse.marketplace.payment;

import com.fmi.springcourse.marketplace.cart.CartService;
import com.fmi.springcourse.marketplace.order.OrderRepository;
import com.fmi.springcourse.marketplace.order.entity.Order;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/stripe")
@RequiredArgsConstructor
public class WebhookController {
    private final OrderRepository orderRepo;

    private final CartService cartService;

    @Value("${stripe.webhook.secret}")
    private String endpointSecret;

    // Inside WebhookController.java
    @PostMapping("/webhook")
    public ResponseEntity<String> handleStripeWebhook(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String sigHeader) {

        Event event;
        try {
            event = Webhook.constructEvent(payload, sigHeader, endpointSecret);
        } catch (SignatureVerificationException e) {
            return ResponseEntity.badRequest().body("Invalid signature");
        }

        // 🚨 ADD THIS GLOBAL TRY-CATCH BLOCK TO EXPOSE THE ERROR
        try {
            if ("checkout.session.completed".equals(event.getType())) {
                EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();

                if (dataObjectDeserializer.getObject().isPresent()) {
                    Session session = (Session) dataObjectDeserializer.getObject().get();

                    System.out.println("Executing handleSuccessfulPayment for session: " + session.getId());
                    handleSuccessfulPayment(session);
                    System.out.println("handleSuccessfulPayment executed completely without errors!");
                }
            }
        } catch (Exception e) {
            // This forces your terminal to print the exact filename and line number that crashed
            System.err.println("CRITICAL ERROR IN STRIPE WEBHOOK PROCESSING:");
            e.printStackTrace();

            // Return a 500 error during debugging so Stripe knows your code failed
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

        return ResponseEntity.ok("Success");
    }

    private void handleSuccessfulPayment(Session session) {

        String sessionId = session.getId();

        // Find the pending database record matching this Stripe session hook
        Order order = orderRepo.findByStripeSessionId(sessionId)
                .orElseThrow(() -> new RuntimeException("Order not found for session: " + sessionId));

        // Update status to Paid
        order.setStatus("PAID");
        orderRepo.save(order);

        // Empty the user's shopping cart now that transaction clearance is verified
        cartService.emptyUserCart(order.getUser());

        System.out.println("Fulfillment complete for order ID: " + order.getId());
    }
}
