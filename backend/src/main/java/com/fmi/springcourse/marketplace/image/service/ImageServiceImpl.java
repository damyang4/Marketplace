package com.fmi.springcourse.marketplace.image.service;

import com.fmi.springcourse.marketplace.exception.AccessDeniedException;
import com.fmi.springcourse.marketplace.image.Image;
import com.fmi.springcourse.marketplace.image.ImageDto;
import com.fmi.springcourse.marketplace.image.repo.DbImageRepository;
import com.fmi.springcourse.marketplace.image.repo.S3ImageRepository;
import com.fmi.springcourse.marketplace.product.entity.Product;
import com.fmi.springcourse.marketplace.exception.EntityNotFoundException;
import com.fmi.springcourse.marketplace.exception.ImageUploadException;
import com.fmi.springcourse.marketplace.product.ProductRepository;
import com.fmi.springcourse.marketplace.user.entity.User;
import com.fmi.springcourse.marketplace.util.FileTypeValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.unit.DataSize;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class ImageServiceImpl implements ImageService {
	@Value("${spring.servlet.multipart.max-file-size}")
	private DataSize maxImageSize;
	
	private final S3ImageRepository s3ImageRepository;
	private final DbImageRepository dbImageRepository;
	private final ProductRepository productRepository;
	
	public ImageServiceImpl(S3ImageRepository s3ImageRepository,
	                        DbImageRepository dbImageRepository,
	                        ProductRepository productRepository) {
		this.s3ImageRepository = s3ImageRepository;
		this.dbImageRepository = dbImageRepository;
		this.productRepository = productRepository;
	}
	
	@Transactional
	@Override
	public List<ImageDto> uploadImages(List<MultipartFile> images, Long productId, User user) {
		validateImages(images);
		
		if (images.size() == 1) {
			return uploadSingleImage(images, productId, user);
		}
		
		var names = s3ImageRepository.uploadMultipleImages(images);
		
		if (productId != null) {
			Product product = productRepository.findById(productId)
				.orElseThrow(() -> new EntityNotFoundException("Product not found."));
			
			if (!product.getUser().equals(user)) {
				throw new AccessDeniedException("Illegal operation.");
			}
			
			var imgList = names.stream()
				.map(name -> new Image(name, product))
				.toList();
			
			product.getAdditionalImages()
				.addAll(imgList);
			productRepository.save(product);
		}
		
		return names.stream()
			.map(ImageDto::new)
			.toList();
	}
	
	private void validateImages(List<MultipartFile> images) {
		for (MultipartFile img : images) {
			if (!isCorrectFileSize(img)) {
				throw new ImageUploadException("Image "
					+ img.getName()
					+ " size must be at most "
					+ maxImageSize.toMegabytes()
				);
			}
			
			if (!FileTypeValidator.isAllowedImage(img)) {
				throw new ImageUploadException("Invalid content type.");
			}
		}
	}
	
	private List<ImageDto> uploadSingleImage(List<MultipartFile> images, Long productId, User user) {
		String name = s3ImageRepository.singleImageUpload(images.getFirst());
		
		if (productId != null) {
			Product product = productRepository.findById(productId)
				.orElseThrow(() -> new EntityNotFoundException("No such product"));
			
			if (!product.getUser().equals(user)) {
				throw new AccessDeniedException("Illegal operation.");
			}
			product.getAdditionalImages()
				.add(new Image(name, product));
			
			productRepository.save(product);
			
		}
		
		return List.of(new ImageDto(name));
	}
	
	private boolean isCorrectFileSize(MultipartFile file) {
		return file.getSize() <= maxImageSize.toBytes();
	}
	
	@Transactional
	@Override
	public void removeImage(String nameInBucket, User user) {
		var image = dbImageRepository.findByNameInBucket(nameInBucket)
				.orElseThrow(() -> new EntityNotFoundException("Image does not exist."));
		
		var owner = image.getProduct().getUser();
		if (!owner.equals(user)) {
			throw new AccessDeniedException("Illegal operation.");
		}
		
		s3ImageRepository.removeImage(nameInBucket);
		dbImageRepository.deleteByNameInBucket(nameInBucket);
	}
}
