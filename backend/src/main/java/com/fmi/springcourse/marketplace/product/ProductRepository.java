package com.fmi.springcourse.marketplace.product;

import com.fmi.springcourse.marketplace.product.entity.Product;
import com.fmi.springcourse.marketplace.product.entity.ProductType;
import com.fmi.springcourse.marketplace.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
	Optional<Product> getBySlug(String slug);
	
	List<Product> id(Long id);
	
	Page<Product> findByType(Pageable pageable, ProductType productType);
	
	Page<Product> findByUser(User user, Pageable pageable);
}
