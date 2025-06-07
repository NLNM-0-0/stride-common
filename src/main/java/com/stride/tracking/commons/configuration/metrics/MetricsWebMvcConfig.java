package com.stride.tracking.commons.configuration.metrics;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
// Must inject serviceName and meterRegistry to use
public class MetricsWebMvcConfig implements WebMvcConfigurer {

    private final MeterRegistry meterRegistry;
    private final String serviceName;

    public MetricsWebMvcConfig(
            MeterRegistry meterRegistry,
            @Value("${app.name:unknown-service}") String serviceName
    ) {
        this.meterRegistry = meterRegistry;
        this.serviceName = serviceName;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new MetricsInterceptor(meterRegistry, serviceName));
    }
}
