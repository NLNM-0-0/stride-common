package com.stride.tracking.commons.configuration.metrics;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerMapping;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@RequiredArgsConstructor
public class MetricsFilter extends OncePerRequestFilter {
    private static final String HTTP_API_REQUESTS = "http.api.requests";
    private static final String HTTP_API_REQUESTS_ACTIVE = "http.api.requests.active";

    private final MeterRegistry meterRegistry;
    private final String serviceName;

    private final ConcurrentHashMap<String, AtomicInteger> activeRequestsMap = new ConcurrentHashMap<>();

    @Override
    protected void doFilterInternal(
            @Nonnull HttpServletRequest request,
            @Nonnull HttpServletResponse response,
            @Nonnull FilterChain filterChain
    ) throws ServletException, IOException {

        Timer.Sample sample = Timer.start(meterRegistry);

        String pattern = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
        if (pattern == null) {
            pattern = request.getRequestURI();
        }

        AtomicInteger activeRequests = activeRequestsMap.computeIfAbsent(pattern, p -> {
            AtomicInteger ai = new AtomicInteger(0);
            meterRegistry.gauge(HTTP_API_REQUESTS_ACTIVE, ai);
            return ai;
        });

        activeRequests.incrementAndGet();

        try {
            filterChain.doFilter(request, response);
        } finally {
            activeRequests.decrementAndGet();

            Timer timer = Timer.builder(HTTP_API_REQUESTS)
                    .tags("method", request.getMethod(),
                            "api", pattern,
                            "status", String.valueOf(response.getStatus()),
                            "service", serviceName)
                    .publishPercentileHistogram(true)
                    .publishPercentiles(0.5, 0.95, 0.99)
                    .register(meterRegistry);

            sample.stop(timer);
        }
    }
}
