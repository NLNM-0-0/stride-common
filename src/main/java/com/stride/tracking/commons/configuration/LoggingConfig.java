package com.stride.tracking.commons.configuration;

import com.stride.tracking.commons.constants.CustomHeaders;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Component
public class LoggingConfig extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {
        Optional<String> requestId = Optional.ofNullable(request.getHeader(CustomHeaders.X_REQUEST_ID));

        if (requestId.isEmpty() || !requestId.get().isBlank()) {
            requestId = Optional.of(UUID.randomUUID().toString());
        }

        response.addHeader(CustomHeaders.X_REQUEST_ID, requestId.get());

        filterChain.doFilter(request, response);
    }
}
