package com.fmi.springcourse.marketplace.product.dto;

import com.fmi.springcourse.marketplace.product.entity.ProductType;

public record ProductTypeDto(String code, String title) {
	public ProductTypeDto(ProductType type) {
		this(type.name(), type.getTitle());
	}
}
