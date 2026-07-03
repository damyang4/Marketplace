package com.fmi.springcourse.marketplace.order;

import com.fmi.springcourse.marketplace.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findByStripeSessionId(String stripeSessionId);

    // "JOIN FETCH" instructs Hibernate to load the User data right away in 1 query!
    @Query("SELECT o FROM Order o JOIN FETCH o.user WHERE o.id = :orderId")
    Optional<Order> findByIdWithUser(@Param("orderId") Long orderId);
}
