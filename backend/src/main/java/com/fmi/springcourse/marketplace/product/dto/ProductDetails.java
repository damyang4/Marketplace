package com.fmi.springcourse.marketplace.product.dto;

import com.fmi.springcourse.marketplace.image.ImageDto;
import com.fmi.springcourse.marketplace.product.entity.Product;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record ProductDetails(Long id,
                             String slug,
                             String name,
                             String description,
                             BigDecimal price,
                             Integer quantity,
                             ProductTypeDto type,
                             LocalDateTime createdAt,
                             String mainImage,
                             List<ImageDto> additionalImages) {
	public ProductDetails(Product product) {
		List<ImageDto> images = null;
		
		if (product.getAdditionalImages() != null) {
			images = product.getAdditionalImages()
				.stream()
				.map(ImageDto::new)
				.toList();
		}
		
		this(product.getId(),
			product.getSlug(),
			product.getName(),
			product.getDescription(),
			product.getPrice(),
			product.getQuantity(),
			new
				
				ProductTypeDto(product.getType()),
			product.getCreatedAt(),
			product.getMainImage(),
			images);
	}
}
