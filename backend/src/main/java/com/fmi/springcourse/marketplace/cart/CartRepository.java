package com.fmi.springcourse.marketplace.cart;

import com.fmi.springcourse.marketplace.cart.entity.Cart;
import com.fmi.springcourse.marketplace.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUser(User user);

    Optional<Cart> findByGuestToken(String guestToken);
}
