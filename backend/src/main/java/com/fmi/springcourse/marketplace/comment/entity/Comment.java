//package com.fmi.springcourse.marketplace.comment.entity;
//
//import com.fmi.springcourse.marketplace.product.entity.Product;
//import com.fmi.springcourse.marketplace.user.entity.User;
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
//import jakarta.validation.constraints.NotBlank;
//import jakarta.validation.constraints.NotNull;
//import lombok.AccessLevel;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//
//import java.util.Objects;
//import java.util.UUID;
//
//@Entity
//@Table(name = "comments")
//@NoArgsConstructor
//public class Comment {
//
//    @Id
//    @Setter(AccessLevel.NONE)
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "commentId", updatable = false, nullable = false)
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
//    @NotBlank
//    @Column(name = "commentText", nullable = false)
//    private String commentText;
//
//    private void validate(UUID userId, UUID productId, String commentText) {
//        if (userId == null) {
//            throw new IllegalArgumentException("User id cannot be null");
//        }
//
//        if (productId == null) {
//            throw new IllegalArgumentException("Product id cannot be null");
//        }
//
//        if (commentText == null || commentText.isBlank()) {
//            throw new IllegalArgumentException("Comment text cannot be null or empty");
//        }
//    }
//
//    public Comment(UUID userId, UUID product, String commentText) {
//        validate(userId, product, commentText);
//
//        this.userId = userId;
//        this.productId = productId;
//        this.commentText = commentText;
//    }
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        Comment comment = (Comment) o;
//        return id != null && Objects.equals(id, comment.id);
//    }
//
//    public Integer getId() {
//        return id;
//    }
//
//    public UUID getUserId() {
//        return userId;
//    }
//
//    public UUID getProductId() {
//        return productId;
//    }
//
//    public String getCommentText() {
//        return commentText;
//    }
//
//
//    public void setId(Integer id) {
//        this.id = id;
//    }
//
//    public void setUserId(UUID userId) {
//        this.userId = userId;
//    }
//
//    public void setProductId(UUID productId) {
//        this.productId = productId;
//    }
//
//    public void setCommentText(String commentText) {
//        this.commentText = commentText;
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hashCode(id);
//    }
//
//    @Override
//    public String toString() {
//        return "Comment{" +
//                "id=" + id +
//                ", commentText='" + commentText + '\'' +
//                '}';
//    }
//}