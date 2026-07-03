package com.fmi.springcourse.marketplace.cart;

import com.fmi.springcourse.marketplace.cart.dto.CartResponse;
import com.fmi.springcourse.marketplace.cart.entity.Cart;
import com.fmi.springcourse.marketplace.product.entity.Product;
import com.fmi.springcourse.marketplace.exception.EntityNotFoundException;
import com.fmi.springcourse.marketplace.exception.OutOfStockException;
import com.fmi.springcourse.marketplace.product.service.ProductServiceImpl;
import com.fmi.springcourse.marketplace.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository repo;
    private final ProductServiceImpl productService;

    private Cart retrieveCart(User user, String guestToken) {
        if (user != null) {
            return repo.findByUser(user).orElse(new Cart(user));
        } else {
            return repo.findByGuestToken(guestToken)
                    .orElse(new Cart(UUID.randomUUID().toString()));
        }
    }

    @Transactional
    public CartResponse addProduct(Long productId, Integer quantity, User user, String guestToken) {
        Cart cart = retrieveCart(user, guestToken);

        Product product = productService.getProductById(productId);
        cart.addItem(product, quantity);
        repo.save(cart);

        return new CartResponse(cart);
    }

    @Transactional
    public CartResponse removeProduct(Long productId, User user, String guestToken) {
        Cart cart = retrieveCart(user, guestToken);

        Product product = productService.getProductById(productId);
        cart.removeItem(product);
        repo.save(cart);
        return new CartResponse(cart);
    }

    @Transactional
    public CartResponse updateQuantity(Long productId, Integer requestedQuantity, User user, String guestToken) {
        Product product = productService.getProductById(productId);
        if (requestedQuantity > product.getQuantity()) {
            throw new OutOfStockException("Only " + product.getQuantity() + " items left in stock");
        }

        Cart cart = retrieveCart(user, guestToken);
        if (cart.getCartItems().isEmpty()) return new CartResponse(cart);

        cart.updateQuantity(product, requestedQuantity);
        repo.save(cart);
        return new CartResponse(cart);
    }

    @Transactional
    public void emptyUserCart(User user) {
        repo.findByUser(user).ifPresent(cart -> {
            cart.getCartItems().clear();
            repo.save(cart);
        });
    }

    @Transactional
    public CartResponse getCart(User user) {
        Cart cart = repo.findByUser(user).orElse(new Cart(user));
        repo.save(cart);
        return new CartResponse(cart);
    }

    @Transactional
    public CartResponse getCartForGuest(String guestToken) {
        Cart cart;

        if (guestToken == null || guestToken.isEmpty()) {
            cart = new Cart(UUID.randomUUID().toString());
        } else {
            cart = repo.findByGuestToken(guestToken)
                    .orElse(new Cart(UUID.randomUUID().toString()));
        }

        repo.save(cart);
        return new CartResponse(cart);
    }

    public Cart getCartByUser(User user) {
        return repo.findByUser(user)
                .orElseThrow(() -> new EntityNotFoundException("User cart not found"));
    }
}
