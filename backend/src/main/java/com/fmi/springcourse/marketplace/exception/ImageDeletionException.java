package com.fmi.springcourse.marketplace.exception;

public class ImageDeletionException extends RuntimeException {
	public ImageDeletionException() {
	}
	
	public ImageDeletionException(String message) {
		super(message);
	}
	
	public ImageDeletionException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public ImageDeletionException(Throwable cause) {
		super(cause);
	}
}
