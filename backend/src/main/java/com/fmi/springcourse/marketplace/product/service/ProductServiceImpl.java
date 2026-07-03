package com.fmi.springcourse.marketplace.product.service;

import com.fmi.springcourse.marketplace.dto.PageResponse;
import com.fmi.springcourse.marketplace.exception.AccessDeniedException;
import com.fmi.springcourse.marketplace.image.Image;
import com.fmi.springcourse.marketplace.image.repo.DbImageRepository;
import com.fmi.springcourse.marketplace.product.ProductRepository;
import com.fmi.springcourse.marketplace.product.dto.ProductTypeDto;
import com.fmi.springcourse.marketplace.product.entity.Product;
import com.fmi.springcourse.marketplace.exception.EntityNotFoundException;
import com.fmi.springcourse.marketplace.exception.OutOfStockException;
import com.fmi.springcourse.marketplace.product.dto.ProductCardDto;
import com.fmi.springcourse.marketplace.product.dto.ProductDetails;
import com.fmi.springcourse.marketplace.product.dto.ProductRequest;
import com.fmi.springcourse.marketplace.image.repo.S3ImageRepository;
import com.fmi.springcourse.marketplace.product.entity.ProductType;
import com.fmi.springcourse.marketplace.user.UserRepository;
import com.fmi.springcourse.marketplace.user.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
public class ProductServiceImpl implements ProductService {
	private static final int MAX_PAGE_SIZE = 100;

	private final ProductRepository productRepository;
	private final S3ImageRepository imageRepository;
	private final DbImageRepository dbImageRepository;
	private final UserRepository userRepository;

	public ProductServiceImpl(ProductRepository productRepository, S3ImageRepository imageRepository,
							  DbImageRepository dbImageRepository, UserRepository userRepository) {
		this.productRepository = productRepository;
		this.imageRepository = imageRepository;
		this.dbImageRepository = dbImageRepository;
		this.userRepository = userRepository;
	}

	@Transactional
	@Override
	public ProductDetails createProduct(ProductRequest req, User user) {
		if (req == null) {
			throw new IllegalArgumentException("Product request can not be null.");
		}

		List<Image> images = getAdditionalImages(req);
		ProductType type = ProductType.valueOf(req.getType().code());

		var product = new Product(req.getName(), req.getDescription(), req.getPrice(), req.getQuantity(),
				type, req.getMainImage().name(), images, user);

		if (images != null && !images.isEmpty()) {
			for (var img : images) {
				img.setProduct(product);
			}
			dbImageRepository.saveAll(images);
		}

		Product savedProduct = productRepository.save(product);

		return new ProductDetails(savedProduct);
	}

	private List<Image> getAdditionalImages(ProductRequest req) {
		if (req.getAdditionalImages() != null) {
			return req.getAdditionalImages()
					.stream()
					.map(dto -> new Image(dto.name(), null))
					.toList();
		}

		return List.of();
	}

	@Override
	@Transactional(readOnly = true)
	public ProductDetails getProductDetailsBySlug(String slug) {
		if (slug == null) {
			throw new IllegalArgumentException("slug can not be null.");
		}

		var product = productRepository.getBySlug(slug)
				.orElseThrow(() -> new EntityNotFoundException("Product not found"));

		return new ProductDetails(product);
	}

	@Override
	public PageResponse<ProductCardDto> listProducts(Pageable pageable) {
		validatePageable(pageable);

		Page<Product> page = productRepository.findAll(pageable);

		return convertToPageResponse(page);
	}

	private PageResponse<ProductCardDto> convertToPageResponse(Page<Product> page) {
		List<ProductCardDto> products = page.get()
				.map(ProductCardDto::new)
				.toList();

		return new PageResponse<>(products, page.getTotalElements(),
				page.getTotalPages());
	}

	@Transactional(readOnly = true)
	@Override
	public PageResponse<ProductCardDto> getProductsByUserId(String profileName, Pageable pageable) {
		validatePageable(pageable);

		var user = userRepository.findByProfileName(profileName)
				.orElseThrow(() -> new EntityNotFoundException("No user with this profile name was found"));

		Page<Product> page = productRepository.findByUser(user, pageable);

		return convertToPageResponse(page);
	}

	@Override
	public PageResponse<ProductCardDto> getProductsByType(ProductType type, Pageable pageable) {
		validatePageable(pageable);

		Page<Product> page = productRepository.findByType(pageable, type);

		return convertToPageResponse(page);
	}

	@Transactional
	@Override
	public void deleteProduct(Long id, User user) {
		var product = productRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Could not find product with this id."));

		if (!product.getUser().equals(user)) {
			throw new AccessDeniedException("You do not have the rights to delete the current product.");
		}

		imageRepository.removeImage(product.getMainImage());

		product.getAdditionalImages()
				.stream()
				.map(Image::getNameInBucket)
				.forEach(imageRepository::removeImage);

		productRepository.deleteById(id);
	}

	@Transactional
	@Override
	public ProductDetails updateProduct(Long id, ProductRequest req, User user) {
		var product = productRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Could not find product with this id."));

		if (!product.getUser().equals(user)) {
			throw new AccessDeniedException("You do not have the rights to update the current product.");
		}

		product.setDescription(req.getDescription());
		product.setName(req.getName());
		product.setPrice(req.getPrice());
		product.setQuantity(req.getQuantity());
		product.setMainImage(req.getMainImage().name());

		return new ProductDetails(productRepository.save(product));
	}

	@Override
	public List<ProductTypeDto> listCategories() {
		return Arrays.stream(ProductType.values())
				.map(ProductTypeDto::new)
				.toList();
	}

	@Override
	@Transactional(readOnly = true)
	public ProductDetails getProductDetailsById(Long id) {
		if (id == null) {
			throw new IllegalArgumentException("Id can not be null.");
		}

		var product = productRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Product not found"));

		return new ProductDetails(product);
	}

	private void validatePageable(Pageable pageable) {
		if (pageable.getPageSize() > MAX_PAGE_SIZE || pageable.getPageSize() <= 0) {
			throw new IllegalArgumentException("Page size is incorrect.");
		}
	}

	public Product getProductById(Long id) {
		return productRepository.findById(id).orElseThrow(() ->
				new EntityNotFoundException("Product with id: " + id + " was not found"));
	}

	@Transactional
	public void deductStock(Long productId, int quantity) {
		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new EntityNotFoundException("Product not found"));

		if (product.getQuantity() < quantity) {
			throw new OutOfStockException("Low stock for: " + product.getName());
		}

		product.setQuantity(product.getQuantity() - quantity);
		productRepository.save(product);
	}
}