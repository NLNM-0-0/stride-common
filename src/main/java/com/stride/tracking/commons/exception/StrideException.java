package com.stride.tracking.commons.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class StrideException extends RuntimeException {
	private final HttpStatus status;
	private final String message;

	public StrideException(HttpStatus status, String message) {
		super(message);
		this.message = message;
		this.status = status;
	}
}
