package com.fmi.springcourse.marketplace.product;

import com.fmi.springcourse.marketplace.dto.PageResponse;
import com.fmi.springcourse.marketplace.dto.StringResponse;
import com.fmi.springcourse.marketplace.product.dto.ProductCardDto;
import com.fmi.springcourse.marketplace.product.dto.ProductDetails;
import com.fmi.springcourse.marketplace.product.dto.ProductRequest;
import com.fmi.springcourse.marketplace.product.dto.ProductTypeDto;
import com.fmi.springcourse.marketplace.product.entity.ProductType;
import com.fmi.springcourse.marketplace.product.service.ProductService;
import com.fmi.springcourse.marketplace.user.entity.User;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {
	private final ProductService service;
	
	public ProductController(ProductService service) {
		this.service = service;
	}
	
	@GetMapping
	public ResponseEntity<PageResponse<ProductCardDto>> listProducts(Pageable pageable) {
		return ResponseEntity.ok(service.listProducts(pageable));
	}
	
	@GetMapping("/categories")
	public ResponseEntity<List<ProductTypeDto>> listCategories() {
		return ResponseEntity.ok(service.listCategories());
	}
	
	@GetMapping("/user/{profileName}")
	public ResponseEntity<PageResponse<ProductCardDto>> getProductsByUser(@PathVariable String profileName,
	                                                                      Pageable pageable) {
		return ResponseEntity.ok(
			service.getProductsByUserId(profileName, pageable)
		);
	}
	
	@GetMapping("/type/{type}")
	public ResponseEntity<PageResponse<ProductCardDto>> getProductsByType(@PathVariable ProductType type,
	                                                                      Pageable pageable) {
		return ResponseEntity.ok(
			service.getProductsByType(type, pageable)
		);
	}

	@GetMapping("/id/{id}")
	public ResponseEntity<ProductDetails> getProductById(@PathVariable Long id) {
		ProductDetails product = service.getProductDetailsById(id);
		return ResponseEntity.ok(product);
	}
	
	@PostMapping
	public ResponseEntity<ProductDetails> createProduct(@AuthenticationPrincipal User user,
	                                                    @Valid @RequestBody ProductRequest product) {
		ProductDetails uploadedProduct = service.createProduct(product, user);
		
		return ResponseEntity
			.status(HttpStatus.CREATED)
			.body(uploadedProduct);
	}
	
	@GetMapping("/{slug}")
	public ResponseEntity<ProductDetails> getProductBySlug(@PathVariable String slug) {
		ProductDetails product = service.getProductDetailsBySlug(slug);
		
		return ResponseEntity.ok(product);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<ProductDetails> updateProduct(
		@AuthenticationPrincipal User user,
		@PathVariable Long id,
		@Valid @RequestBody ProductRequest product
	) {
		ProductDetails updatedProduct = service.updateProduct(id, product, user);
		
		return ResponseEntity.ok(updatedProduct);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<StringResponse> deleteProduct(@AuthenticationPrincipal User user,
	                                                    @PathVariable Long id) {
		service.deleteProduct(id, user);
		
		return ResponseEntity.ok(new StringResponse("Product deleted successfully"));
	}
}
