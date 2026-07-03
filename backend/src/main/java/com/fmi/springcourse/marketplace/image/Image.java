package com.fmi.springcourse.marketplace.image;

import com.fmi.springcourse.marketplace.product.entity.Product;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(
	name = "images"
)
public class Image {
	@EqualsAndHashCode.Include
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;
	
	@Getter
	@Column(unique = true, nullable = false)
	private String nameInBucket;
	
	@Getter
	@Setter
	@ManyToOne
	@JoinColumn(name = "product_id")
	private Product product;
	
	protected Image() {
	}
	
	public Image(String nameInBucket, Product product) {
		this.nameInBucket = nameInBucket;
		this.product = product;
	}
}
