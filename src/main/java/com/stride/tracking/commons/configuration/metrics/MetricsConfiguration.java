package com.stride.tracking.commons.configuration.metrics;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
// Must inject serviceName and meterRegistry to use
public class MetricsConfiguration {

    @Value("${app.name:unknown-service}")
    private String serviceName;

    @Bean
    public MetricsFilter metricsFilter(MeterRegistry meterRegistry) {
        return new MetricsFilter(meterRegistry, serviceName);
    }
}
