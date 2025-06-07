package com.stride.tracking.commons.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stride.tracking.commons.dto.ErrorResponse;
import com.stride.tracking.commons.exception.StrideException;
import feign.FeignException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.function.Supplier;

public class FeignClientHandler {
    private FeignClientHandler() {
    }

    public static <T> T handleExternalCall(
            Supplier<ResponseEntity<T>> supplier,
            HttpStatus httpStatus,
            String errorMessage
    ) {
        ResponseEntity<T> response = supplier.get();

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new StrideException(httpStatus, errorMessage);
        }

        return response.getBody();
    }

    public static <T> T handleInternalCall(
            Supplier<ResponseEntity<T>> supplier,
            HttpStatus httpStatus,
            String errorMessage
    ) {
        try {
            ResponseEntity<T> response = supplier.get();

            if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
                throw new StrideException(httpStatus, errorMessage);
            }

            return response.getBody();

        } catch (FeignException ex) {
            try {
                String body = ex.contentUTF8();

                ObjectMapper mapper = new ObjectMapper();
                mapper.findAndRegisterModules();

                ErrorResponse errorResponse = mapper.readValue(body, ErrorResponse.class);

                throw new StrideException(httpStatus, errorResponse.getMessage());
            } catch (JsonProcessingException parsingEx) {
                throw new StrideException(httpStatus, errorMessage);
            }
        }
    }
}
