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
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class LoggingConfig extends OncePerRequestFilter {
    private final Tracer tracer;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {
        String timezone = request.getHeader(CustomHeaders.X_USER_TIMEZONE);
        if (timezone == null || timezone.isBlank()) {
            timezone = "UTC";
        }
        response.addHeader(CustomHeaders.X_USER_TIMEZONE, timezone);

        Optional.ofNullable(tracer.currentSpan())
                .ifPresent(span -> {
                    String traceId = span.context().traceId();
                    String spanId = span.context().spanId();

                    if (!traceId.isEmpty()) {
                        response.addHeader(CustomHeaders.X_TRACE_ID, traceId);
                    }
                    if (!spanId.isEmpty()) {
                        response.addHeader(CustomHeaders.X_SPAN_ID, spanId);
                    }
                });

        String userId = request.getHeader(CustomHeaders.X_AUTH_USER_ID);
        if (userId != null && !userId.isBlank()) {
            MDC.put(CustomHeaders.X_AUTH_USER_ID, userId);
        }

        filterChain.doFilter(request, response);
    }
}
