package com.fmi.springcourse.marketplace.cart.entity;

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
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
@Table(name = "carts")
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    // FK
    // @NotNull
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id") // nullable = false
    private User user;

    @NotNull
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> cartItems = new ArrayList<>();

    @NotNull
    @Column(nullable = false)
    private BigDecimal totalAmount = BigDecimal.ZERO;

    // UUID generated for anonymous users to track their cart without requiring authentication
    private String guestToken;

    public Cart(User user) {
        this.user = user;
    }

    public Cart(String guestToken) {
        this.guestToken = guestToken;
    }

    private void updateTotal() {
        this.totalAmount = cartItems.stream()
                .map(item -> item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void addItem(Product product, int quantity) {
        Optional<CartItem> existingItem = cartItems.stream()
                .filter(item -> item.getProduct().equals(product))
                .findFirst();

        if (existingItem.isPresent()) {
            existingItem.get().setQuantity(existingItem.get().getQuantity() + quantity);
        } else {
            CartItem newItem = new CartItem(this, product, quantity);
            this.cartItems.add(newItem);
        }

        updateTotal();
    }

    public void removeItem(Product product) {
        boolean removed = this.cartItems.removeIf(item -> item.getProduct().equals(product));

        if (removed) {
            updateTotal();
        }
    }

    public void updateQuantity(Product product, Integer quantity) {
        this.cartItems.stream()
                .filter(item -> item.getProduct().equals(product))
                .findFirst()
                .ifPresent(item -> {
                    if (quantity > 0) {
                        item.setQuantity(quantity);
                        updateTotal();
                    }
                });
    }
}
