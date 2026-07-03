package com.fmi.springcourse.marketplace.order.entity;

import com.fmi.springcourse.marketplace.product.entity.Product;
import com.fmi.springcourse.marketplace.user.entity.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter @Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "orderId", nullable = false)
    private Long id;

    // FK
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

//    @NotEmpty
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    @NotNull
    @Column(nullable = false)
    private LocalDateTime orderDate;

    @NotNull
    @Column(nullable = false)
    private BigDecimal totalAmount;

    @Column(name = "stripe_session_id")
    private String stripeSessionId;

    @NotNull
    @Column(nullable = false)
    private String status; // e.g., "PENDING", "PAID"

    public Order(User user, LocalDateTime orderDate) {
        this.user = user;
        this.orderDate = orderDate;
        this.totalAmount = BigDecimal.ZERO;
        this.status = "PENDING"; // Defaults to pending
    }

    public void addItem(Product product, int quantity) {
        OrderItem item = new OrderItem(this, product, quantity);
        this.orderItems.add(item);
        BigDecimal itemTotal = product.getPrice().multiply(BigDecimal.valueOf(quantity));
        this.totalAmount = totalAmount.add(itemTotal);
    }
}
