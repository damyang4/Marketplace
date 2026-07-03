package com.fmi.springcourse.marketplace.product.service;

import com.fmi.springcourse.marketplace.dto.PageResponse;
import com.fmi.springcourse.marketplace.product.dto.ProductCardDto;
import com.fmi.springcourse.marketplace.product.dto.ProductDetails;
import com.fmi.springcourse.marketplace.product.dto.ProductRequest;
import com.fmi.springcourse.marketplace.product.dto.ProductTypeDto;
import com.fmi.springcourse.marketplace.product.entity.ProductType;
import com.fmi.springcourse.marketplace.user.entity.User;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {
	ProductDetails createProduct(ProductRequest product, User user);
	
	ProductDetails getProductDetailsBySlug(String slug);
	
	PageResponse<ProductCardDto> listProducts(Pageable pageable);
	
	PageResponse<ProductCardDto> getProductsByUserId(String profileName, Pageable pageable);
	
	PageResponse<ProductCardDto> getProductsByType(ProductType type, Pageable pageable);
	
	void deleteProduct(Long id, User user);
	
	ProductDetails updateProduct(Long id, ProductRequest req, User user);
	
	List<ProductTypeDto> listCategories();

    ProductDetails getProductDetailsById(Long id);
}
