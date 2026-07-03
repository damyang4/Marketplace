package com.fmi.springcourse.marketplace.image.repo;

import com.fmi.springcourse.marketplace.exception.ImageDeletionException;
import com.fmi.springcourse.marketplace.exception.ImageUploadException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Repository
public class S3ImageRepository {
	private static final String FOLDER = "image/";
	private final S3Client client;
	
	@Value("${r2.bucket-name}")
	private String bucketName;
	
	public S3ImageRepository(S3Client client) {
		this.client = client;
	}
	
	public List<String> uploadMultipleImages(List<MultipartFile> images) {
		return images.parallelStream()
			.map(this::putObject)
			.toList();
	}
	
	private String putObject(MultipartFile file) {
		try {
			String uuid = UUID.randomUUID().toString();
			String key = FOLDER + uuid;
			
			PutObjectRequest putObjectRequest = PutObjectRequest.builder()
				.bucket(bucketName)
				.key(key)
				.contentType(file.getContentType())
				.build();
			
			client.putObject(
				putObjectRequest,
				RequestBody.fromBytes(file.getBytes())
			);
			
			return uuid;
		} catch (IOException | S3Exception e) {
			throw new ImageUploadException("Could not upload image", e);
		}
	}
	
	public String singleImageUpload(MultipartFile img) {
		return putObject(img);
	}
	
	public void removeImage(String nameInBucket) {
		try {
			DeleteObjectRequest request = DeleteObjectRequest.builder()
				.bucket(bucketName)
				.key(FOLDER + nameInBucket)
				.build();
			
			client.deleteObject(request);
		} catch (S3Exception e) {
			throw new ImageDeletionException("Could not delete image", e);
		}
	}
}
