package com.fmi.springcourse.marketplace.order;

import com.fmi.springcourse.marketplace.cart.CartService;
import com.fmi.springcourse.marketplace.cart.entity.Cart;
import com.fmi.springcourse.marketplace.cart.entity.CartItem;
import com.fmi.springcourse.marketplace.exception.AccessDeniedException;
import com.fmi.springcourse.marketplace.exception.CartEmptyException;
import com.fmi.springcourse.marketplace.exception.EntityNotFoundException;
import com.fmi.springcourse.marketplace.order.dto.OrderResponse;
import com.fmi.springcourse.marketplace.order.entity.Order;
import com.fmi.springcourse.marketplace.product.service.ProductServiceImpl;
import com.fmi.springcourse.marketplace.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepo;
    private final CartService cartService;
    private final ProductServiceImpl productService;

    private Order fillOrder(Cart cart) {
        Order order = new Order(cart.getUser(), LocalDateTime.now());

        cart.getCartItems().forEach(item -> order.addItem(item.getProduct(), item.getQuantity()));

        return order;
    }

    @Transactional
    public OrderResponse createOrderFromCart(User user) {
        Cart cart = cartService.getCartByUser(user);
        System.out.println("Creating order from cart: " + cart);

        if (cart.getCartItems().isEmpty()) {
            throw new CartEmptyException("Cannot checkout an empty cart");
        }

        for (CartItem item: cart.getCartItems()) {
            productService.deductStock(item.getProduct().getId(), item.getQuantity());
        }

        Order order = fillOrder(cart);
        Order savedOrder = orderRepo.save(order);

        cartService.emptyUserCart(cart.getUser());

        return new OrderResponse(savedOrder);
    }

    @Transactional
    public Order createPendingOrderFromCart(User user) {
        Cart cart = cartService.getCartByUser(user);

        if (cart.getCartItems().isEmpty()) {
            throw new CartEmptyException("Cannot checkout an empty cart");
        }

        // Deduct stock immediately to reserve the items while they are paying on Stripe
        for (CartItem item : cart.getCartItems()) {
            productService.deductStock(item.getProduct().getId(), item.getQuantity());
        }

        Order order = fillOrder(cart);
        order.setStatus("PENDING"); // Mark explicitly as pending

        return orderRepo.save(order);
    }

    @Transactional(readOnly = true)
    public OrderResponse getOrderForUser(Long orderId, User user) {
        Order order = orderRepo.findByIdWithUser(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));

        // Security check: Make sure this order belongs to the logged-in user
        if (!order.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("You do not have permission to view this order");
        }

        return new OrderResponse(order);
    }

    public void saveOrder(Order order) {
        orderRepo.save(order);
    }
}
