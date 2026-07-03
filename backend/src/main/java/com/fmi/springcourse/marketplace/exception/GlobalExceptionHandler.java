package com.fmi.springcourse.marketplace.exception;

import com.fmi.springcourse.marketplace.dto.ExceptionResponse;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
//	@ExceptionHandler(MethodArgumentNotValidException.class)
//	public ResponseEntity<ExceptionResponse> handleMethodValidation(MethodArgumentNotValidException ex) {
//		var fieldError = ex.getBindingResult().getFieldError();
//
//		if (fieldError != null) {
//			return ResponseEntity.badRequest()
//				.body(new ExceptionResponse(fieldError.getDefaultMessage()));
//		}
//
//		var globalError = ex.getBindingResult().getGlobalError();
//
//		if (globalError != null) {
//			return ResponseEntity.badRequest()
//				.body(new ExceptionResponse(globalError.getDefaultMessage()));
//		}
//
//		return ResponseEntity.badRequest()
//			.body(new ExceptionResponse("Validation failed"));
//	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
		Map<String, String> errors = new HashMap<>();

		// getFieldErrors() returns a list of all failed fields
		ex.getBindingResult().getFieldErrors().forEach(error -> {
			errors.put(error.getField(), error.getDefaultMessage());
		});

		return ResponseEntity.badRequest().body(errors);
	}

	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity<ExceptionResponse> handleEntityNotFoundException(EntityNotFoundException ex) {
		return ResponseEntity.badRequest()
			.body(new ExceptionResponse(ex.getMessage()));
	}
	
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ExceptionResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
		return ResponseEntity.badRequest()
			.body(new ExceptionResponse(ex.getMessage()));
	}

	@ExceptionHandler(UserAlreadyExistsException.class)
	public ResponseEntity<ExceptionResponse> handleEntityAlreadyExistException(UserAlreadyExistsException ex) {
		return ResponseEntity.badRequest()
				.body(new ExceptionResponse(ex.getMessage()));
	}

	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<ExceptionResponse> handleBadCredentialsException(BadCredentialsException ex) {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
				.body(new ExceptionResponse("Invalid credentials"));
	}
	
	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<ExceptionResponse> handleAccessDenied(AccessDeniedException ex) {
		return ResponseEntity.status(HttpStatus.FORBIDDEN)
			.body(new ExceptionResponse(ex.getMessage()));
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<Map<String, String>> handleEntityValidation(ConstraintViolationException ex) {
		Map<String, String> errors = new HashMap<>();
		ex.getConstraintViolations().forEach(violation ->
				errors.put(violation.getPropertyPath().toString(), violation.getMessage()));
		return ResponseEntity.badRequest()
				.body(errors);
	}

	@ExceptionHandler(OutOfStockException.class)
	public ResponseEntity<ExceptionResponse> handleOutOfStock(OutOfStockException ex) {
		return ResponseEntity.status(HttpStatus.CONFLICT)
				.body(new ExceptionResponse(ex.getMessage()));
	}

	@ExceptionHandler(CartEmptyException.class)
	public ResponseEntity<ExceptionResponse> handleEmptyCart(CartEmptyException ex) {
		return ResponseEntity.badRequest()
				.body(new ExceptionResponse(ex.getMessage()));
	}
}
