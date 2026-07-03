package com.fmi.springcourse.marketplace.payment;

import com.fmi.springcourse.marketplace.order.OrderService;
import com.fmi.springcourse.marketplace.order.entity.Order;
import com.fmi.springcourse.marketplace.user.entity.User;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.model.checkout.Session;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.checkout.SessionCreateParams;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final OrderService orderService;

    @Value("${stripe.secretKey}")
    private String secretKey;

    @PostConstruct
    public void init() {
        // Set the Stripe API key globally for the Stripe SDK
        Stripe.apiKey = secretKey;
    }

    public String createCheckoutSession(long amountInCents, String currency, User user) throws StripeException {

        // 1. Create and persist the pending order in the DB
        Order pendingOrder = orderService.createPendingOrderFromCart(user);

        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:4200/order-success?order_id=" + pendingOrder.getId())
                .setCancelUrl("http://localhost:4200/cart")
                .setShippingAddressCollection(
                        SessionCreateParams.ShippingAddressCollection.builder()
                                .addAllowedCountry(SessionCreateParams.ShippingAddressCollection.AllowedCountry.BG)
                                .addAllowedCountry(SessionCreateParams.ShippingAddressCollection.AllowedCountry.US)
                                .build()
                )
                .addLineItem(
                        SessionCreateParams.LineItem.builder()
                                .setQuantity(1L)
                                .setPriceData(
                                        SessionCreateParams.LineItem.PriceData.builder()
                                                .setCurrency(currency)
                                                .setUnitAmount(amountInCents)
                                                .setProductData(
                                                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                .setName("Marketplace Total Order")
                                                                .build()
                                                )
                                                .build()
                                )
                                .build()
                )
                .build();

        // Generate the checkout session via the Stripe API
        Session session = Session.create(params);

        // Update the pending order with the actual Stripe session ID and save it
        pendingOrder.setStripeSessionId(session.getId());
        orderService.saveOrder(pendingOrder); // Ensure your OrderService or OrderRepository saves this update!

        return session.getUrl();
    }
}
