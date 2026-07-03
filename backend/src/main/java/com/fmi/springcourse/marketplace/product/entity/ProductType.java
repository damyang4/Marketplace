package com.fmi.springcourse.marketplace.product.entity;

import lombok.Getter;

@Getter
public enum ProductType {
	FRUITS("Fruits"),
	VEGETABLES("Vegetables"),
	MEAT("Meat"),
	BREADS("Breads & bakery"),
	SEAFOOD("Seafood"),
	HONEY("Honey & jams"),
	RICE("Rice & grains"),
	BEVERAGES("Beverages"),
	DAIRY("Dairy & eggs"),
	PASTA("Pasta & noodles"),
	SWEETS("Sweets"),
	NUTS("Nuts and seeds");
	
	private final String title;
	
	ProductType(String title) {
		this.title = title;
	}
	
}
