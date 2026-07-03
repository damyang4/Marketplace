package com.fmi.springcourse.marketplace.image;

import com.fmi.springcourse.marketplace.dto.ExceptionResponse;
import com.fmi.springcourse.marketplace.dto.StringResponse;
import com.fmi.springcourse.marketplace.exception.ImageDeletionException;
import com.fmi.springcourse.marketplace.exception.ImageUploadException;
import com.fmi.springcourse.marketplace.image.service.ImageService;
import com.fmi.springcourse.marketplace.user.entity.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/images")
public class ImageController {
	private final ImageService service;
	
	public ImageController(ImageService service) {
		this.service = service;
	}
	
	@PostMapping
	public ResponseEntity<List<ImageDto>> upload(@AuthenticationPrincipal User user,
	                                             @RequestParam("images") List<MultipartFile> images,
	                                             @RequestParam(value = "productId", required = false) Long productId
	) {
		var ids = service.uploadImages(images, productId, user);
		
		return ResponseEntity.ok(ids);
	}
	
	@DeleteMapping("/{name}")
	public ResponseEntity<StringResponse> removeHandler(@AuthenticationPrincipal User user,
	                                                    @PathVariable String name) {
		service.removeImage(name, user);
		
		return ResponseEntity.ok(
			new StringResponse("Image deleted successfully")
		);
	}
	
	@ExceptionHandler(ImageUploadException.class)
	public ResponseEntity<ExceptionResponse> imageUploadExceptionHandler(ImageUploadException e) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
			.body(new ExceptionResponse(e.getMessage()));
	}
	
	@ExceptionHandler(ImageDeletionException.class)
	public ResponseEntity<ExceptionResponse> imageUploadExceptionHandler(ImageDeletionException e) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
			.body(new ExceptionResponse(e.getMessage()));
	}
	
}
