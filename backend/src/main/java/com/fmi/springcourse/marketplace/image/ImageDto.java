package com.fmi.springcourse.marketplace.image;

public record ImageDto(String name) {
	public ImageDto(Image image) {
		this(image.getNameInBucket());
	}
}
