package com.stride.tracking.commons.exception;

import com.stride.tracking.commons.constants.Message;
import com.stride.tracking.commons.dto.ErrorResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.CompletionException;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
	private ResponseEntity<ErrorResponse> createErrorResponse(Exception exception,
															  WebRequest webRequest,
															  HttpStatus status) {
		ErrorResponse errorResponse = new ErrorResponse(new Date(),
														status,
														exception.getMessage(),
														webRequest.getDescription(false));
		return new ResponseEntity<>(errorResponse, status);
	}

	private ResponseEntity<ErrorResponse> createErrorResponse(String message,
															  WebRequest webRequest,
															  HttpStatus status) {
		ErrorResponse errorResponse = new ErrorResponse(new Date(),
														status,
														message,
														webRequest.getDescription(false));
		return new ResponseEntity<>(errorResponse, status);
	}

	@ExceptionHandler(DuplicateKeyException.class)
	public ResponseEntity<ErrorResponse> handleDuplicateKeyExceptionException(DuplicateKeyException exception,
																			 WebRequest webRequest) {
		return createErrorResponse(exception, webRequest, HttpStatus.CONFLICT);
	}

	@ExceptionHandler(MissingRequiredFieldException.class)
	public ResponseEntity<ErrorResponse> handleMissingRequiredFieldException(MissingRequiredFieldException exception,
																			  WebRequest webRequest) {
		return createErrorResponse(exception, webRequest, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(ResourceAlreadyExistException.class)
	public ResponseEntity<ErrorResponse> handleResourceAlreadyExistException(ResourceAlreadyExistException exception,
																		 WebRequest webRequest) {
		return createErrorResponse(exception, webRequest, HttpStatus.CONFLICT);
	}

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException exception,
																		 WebRequest webRequest) {
		return createErrorResponse(exception, webRequest, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(StrideException.class)
	public ResponseEntity<ErrorResponse> handleRequestException(StrideException exception,
																WebRequest webRequest) {
		return createErrorResponse(exception, webRequest, exception.getStatus());
	}

	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<ErrorResponse> handleAccessDeniedException(WebRequest webRequest) {
		return createErrorResponse(Message.USER_DONT_HAVE_FEATURE, webRequest, HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(CompletionException.class)
	public ResponseEntity<ErrorResponse> handleCompletionException(CompletionException exception,
																   WebRequest webRequest) {
		Throwable cause = exception.getCause();
		if (cause instanceof StrideException strideException) {
			return handleRequestException(strideException, webRequest);
		}

		return createErrorResponse(exception, webRequest, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleGlobalException(Exception exception,
															   WebRequest webRequest) {
		return createErrorResponse(exception, webRequest, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
															   @NonNull HttpHeaders headers,
															   @NonNull HttpStatusCode status,
															   @NonNull WebRequest request) {
		HashMap<String, String> errors = new HashMap<>();
		for (ObjectError error : ex.getAllErrors()) {
			String fieldName = ((FieldError) error).getField();
			String fieldMessage = error.getDefaultMessage();
			errors.put(fieldName, fieldMessage);
		}

		StringBuilder concatenated = new StringBuilder();
		for (String value : errors.values()) {
			concatenated.append(value).append(". ");
		}
		if (!concatenated.isEmpty()) {
			concatenated.setLength(concatenated.length() - 2);
		}
		HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
		ErrorResponse errorResponse = new ErrorResponse(new Date(),
														httpStatus,
														concatenated.toString(),
														request.getDescription(false));
		return new ResponseEntity<>(errorResponse, httpStatus);
	}
}
