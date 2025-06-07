package com.stride.tracking.commons.configuration.metrics;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

@RequiredArgsConstructor
public class MetricsInterceptor implements HandlerInterceptor {
    private static final String HTTP_API_REQUESTS = "http.api.requests";

    private static final String API_PATTERN_KEY = "pattern";
    private static final String TIMER_SAMPLE_KEY = "timerSample";

    private final MeterRegistry meterRegistry;
    private final String serviceName;


    @Override
    public boolean preHandle(
            HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull Object handler
    ) {
        Timer.Sample sample = Timer.start(meterRegistry);
        request.setAttribute(TIMER_SAMPLE_KEY, sample);

        String pattern = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
        if (pattern == null) {
            pattern = request.getRequestURI();
        }
        request.setAttribute(API_PATTERN_KEY, pattern);

        return true;
    }

    @Override
    public void afterCompletion(
            HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull Object handler,
            Exception ex
    ) {
        Timer.Sample sample = (Timer.Sample) request.getAttribute(TIMER_SAMPLE_KEY);
        String pattern = (String) request.getAttribute(API_PATTERN_KEY);

        if (sample != null && pattern != null) {
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
