package com.stride.tracking.commons.configuration.log;

import com.stride.tracking.commons.constants.CustomHeaders;
import io.micrometer.tracing.Tracer;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class LoggingConfig extends OncePerRequestFilter {
    private final Tracer tracer;

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

        String timezone = request.getHeader(CustomHeaders.X_USER_TIMEZONE);
        if (timezone == null || timezone.isBlank()) {
            timezone = "UTC";
        }
        response.addHeader(CustomHeaders.X_USER_TIMEZONE, timezone);

        String traceId = Objects.requireNonNull(tracer.currentSpan()).context().traceId();
        response.addHeader("Trace-Id", traceId);

        MDC.put(CustomHeaders.X_REQUEST_ID, requestId);
        String userId = request.getHeader(CustomHeaders.X_AUTH_USER_ID);
        if (userId != null && !userId.isBlank()) {
            MDC.put(CustomHeaders.X_AUTH_USER_ID, userId);
        }

        filterChain.doFilter(request, response);
    }
}
