package com.stride.tracking.commons.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Date;

@AllArgsConstructor
@Getter
public class ErrorResponse {
	private Date timestamp;
	private HttpStatus status;
	private String message;
	private String detail;
}
