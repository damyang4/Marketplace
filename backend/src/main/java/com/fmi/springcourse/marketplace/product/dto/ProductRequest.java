package com.fmi.springcourse.marketplace.product.dto;

import com.fmi.springcourse.marketplace.image.ImageDto;
import com.fmi.springcourse.marketplace.product.entity.Product;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Setter
@Getter
public class ProductRequest {
	@NotBlank(message = "Name can not be blank.")
	private String name;
	
	@NotBlank(message = "Description can not be blank.")
	@Size(max = Product.DESCRIPTION_MAX_LENGTH,
		message = "Description can not be more than " + Product.DESCRIPTION_MAX_LENGTH + " description")
	private String description;
	
	@NotNull
	@DecimalMin(value = "0.00", message = "Price can not be a negative number.")
	private BigDecimal price;
	
	@NotNull
	@Min(value = 0, message = "Quantity can not be a negative number")
	private Integer quantity;
	
	@NotNull
	private ProductTypeDto type;
	
	@NotNull
	private ImageDto mainImage;
	
	private List<ImageDto> additionalImages;
	
	public ProductRequest() {
	}
}
