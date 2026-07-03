package com.fmi.springcourse.marketplace.util;

import org.apache.tika.Tika;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

public class FileTypeValidator {
	private static final Tika TIKA = new Tika();
	
	private static final Set<String> ALLOWED_TYPES = Set.of(
		"image/jpeg",
		"image/png",
		"image/webp"
	);
	
	public static boolean isAllowedImage(MultipartFile file) {
		if (file == null || file.isEmpty()) {
			return false;
		}
		
		try (InputStream is = file.getInputStream()) {
			String detectedType = TIKA.detect(is);
			return ALLOWED_TYPES.contains(detectedType);
		} catch (IOException e) {
			return false;
		}
	}
}
