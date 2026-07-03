package com.fmi.springcourse.marketplace.product.entity;

import com.fmi.springcourse.marketplace.image.Image;
import com.fmi.springcourse.marketplace.user.entity.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "products",
	indexes = @Index(name = "idx_slug", columnList = "slug", unique = true)
)
public class Product {
	public static final int DESCRIPTION_MAX_LENGTH = 2000;
	private static final int PRICE_PRECISION = 10;
	
	@Id
	@EqualsAndHashCode.Include
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false, unique = true)
	private String slug;
	
	@Setter
	@Column(nullable = false)
	private String name;
	
	@Setter
	@Column(nullable = false, length = DESCRIPTION_MAX_LENGTH)
	private String description;
	
	@Setter
	@Column(nullable = false, precision = PRICE_PRECISION, scale = 2)
	private BigDecimal price;
	
	@Setter
	@Column(nullable = false)
	private Integer quantity;
	
	@CreationTimestamp
	@Column(nullable = false, updatable = false)
	private LocalDateTime createdAt;
	
	@Setter
	@Column(nullable = false)
	private String mainImage;
	
	@Enumerated(EnumType.STRING)
	private ProductType type;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "product")
	private List<Image> additionalImages = new ArrayList<>();
	
	@Getter
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;
	
	protected Product() {
	}
	
	public Product(String name, String description, BigDecimal price, Integer quantity, ProductType type,
	               String mainImage, List<Image> additionalImages, User user) {
		this.name = name;
		this.description = description;
		this.price = price;
		this.quantity = quantity;
		this.type = type;
		this.mainImage = mainImage;
		this.additionalImages = additionalImages;
		this.user = user;
	}
	
	@PrePersist
	public void prePersist() {
		if (slug == null || slug.isBlank()) {
			slug = UUID.randomUUID().toString();
		}
	}
}