package com.fmi.springcourse.marketplace.image.service;

import com.fmi.springcourse.marketplace.image.ImageDto;
import com.fmi.springcourse.marketplace.user.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImageService {
	List<ImageDto> uploadImages(List<MultipartFile> images, Long productId, User user);
	
	void removeImage(String nameInBucket, User user);
}
