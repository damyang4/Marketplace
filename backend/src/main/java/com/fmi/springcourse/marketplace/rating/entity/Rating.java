//package com.fmi.springcourse.marketplace.rating.entity;
//
//import jakarta.persistence.Column;
//import jakarta.persistence.Entity;
//import jakarta.persistence.FetchType;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
//import jakarta.persistence.Id;
//import jakarta.persistence.JoinColumn;
//import jakarta.persistence.ManyToOne;
//import jakarta.persistence.Table;
//import jakarta.validation.constraints.Max;
//import jakarta.validation.constraints.Min;
//import jakarta.validation.constraints.NotNull;
//import lombok.AccessLevel;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//
//import java.util.Objects;
//import java.util.UUID;
//
//@Entity
//@Table(name = "ratings")
//@NoArgsConstructor
//public class Rating {
//
//    @Id
//    @Setter(AccessLevel.NONE)
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "ratingId", updatable = false, nullable = false)
//    private Integer id;
//
//    @NotNull
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "userId", nullable = false)
//    private UUID userId;
//
//    @NotNull
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "productId", nullable = false)
//    private UUID productId;
//
//    @NotNull
//    @Min(value = 1, message = "Rating must be at least 1 star")
//    @Max(value = 5, message = "Rating cannot be more than 5 stars")
//    @Column(name = "numberOfStars", nullable = false)
//    private Integer numberOfStars;
//
//    private void validate(UUID userId, UUID productId, Integer numberOfStars) {
//        if (userId == null) {
//            throw new IllegalArgumentException("User cannot be null");
//        }
//
//        if (productId == null) {
//            throw new IllegalArgumentException("Product cannot be null");
//        }
//
//        if (numberOfStars == null || numberOfStars < 1 || numberOfStars > 5) {
//            throw new IllegalArgumentException("Number of stars must be between 1 and 5");
//        }
//    }
//
//    public Rating(UUID userId, UUID productId, Integer numberOfStars) {
//        validate(userId, productId, numberOfStars);
//
//        this.userId = userId;
//        this.productId = productId;
//        this.numberOfStars = numberOfStars;
//    }
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        Rating rating = (Rating) o;
//        return id != null && Objects.equals(id, rating.id);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hashCode(id);
//    }
//
//    @Override
//    public String toString() {
//        return "Rating{" +
//                "id=" + id +
//                ", numberOfStars=" + numberOfStars +
//                '}';
//    }
//
//    public Integer getId() {
//        return id;
//    }
//
//    public UUID getUser() {
//        return userId;
//    }
//
//    public void setUser(UUID userId) {
//        this.userId = userId;
//    }
//
//    public UUID getProduct() {
//        return productId;
//    }
//
//    public void setProduct(UUID productId) {
//        this.productId = productId;
//    }
//
//    public Integer getNumberOfStars() {
//        return numberOfStars;
//    }
//
//    public void setNumberOfStars(Integer numberOfStars) {
//        this.numberOfStars = numberOfStars;
//    }
//}