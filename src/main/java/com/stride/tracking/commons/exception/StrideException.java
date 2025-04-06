package com.stride.tracking.commons.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public class StrideException extends RuntimeException {
	private final HttpStatus status;
	private final String message;
}
