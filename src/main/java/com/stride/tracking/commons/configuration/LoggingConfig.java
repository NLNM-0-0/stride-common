package com.stride.tracking.commons.configuration;

import com.stride.tracking.commons.constants.CustomHeaders;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
public class LoggingConfig extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {
        String requestId = request.getHeader(CustomHeaders.X_REQUEST_ID);
        if (requestId == null || requestId.isBlank()) {
            requestId = UUID.randomUUID().toString();
        }

        response.addHeader(CustomHeaders.X_REQUEST_ID, requestId);

        MDC.put(CustomHeaders.X_REQUEST_ID, requestId);

        String userId = request.getHeader(CustomHeaders.X_AUTH_USER_ID);
        if (userId != null && !userId.isBlank()) {
            MDC.put(CustomHeaders.X_AUTH_USER_ID, userId);
        }

        filterChain.doFilter(request, response);
    }
}
