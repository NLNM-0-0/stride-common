package com.stride.tracking.commons.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stride.tracking.commons.dto.ErrorResponse;
import com.stride.tracking.commons.exception.StrideException;
import feign.FeignException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.function.Supplier;

public class InternalFeignHandler {
    private InternalFeignHandler() {
    }

    public static <T> T callWithErrorHandling(Supplier<ResponseEntity<T>> supplier, String errorMessage) {
        try {
            ResponseEntity<T> response = supplier.get();

            if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
                throw new StrideException(HttpStatus.BAD_REQUEST, errorMessage);
            }

            return response.getBody();

        } catch (FeignException ex) {
            try {
                String body = ex.contentUTF8();

                ObjectMapper mapper = new ObjectMapper();
                mapper.findAndRegisterModules();

                ErrorResponse errorResponse = mapper.readValue(body, ErrorResponse.class);

                throw new StrideException(HttpStatus.BAD_REQUEST, errorResponse.getMessage());
            } catch (JsonProcessingException parsingEx) {
                throw new StrideException(HttpStatus.BAD_REQUEST, errorMessage);
            }
        }
    }
}
