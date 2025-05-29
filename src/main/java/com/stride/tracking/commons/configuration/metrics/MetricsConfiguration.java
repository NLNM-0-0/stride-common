package com.stride.tracking.commons.configuration.metrics;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

@Configuration
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
// Must inject serviceName and meterRegistry to use
public class MetricsConfiguration {

    @Value("${app.name:unknown-service}")
    private String serviceName;

    @Bean
    public FilterRegistrationBean<MetricsFilter> metricsFilter(MeterRegistry meterRegistry) {
        FilterRegistrationBean<MetricsFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new MetricsFilter(meterRegistry, serviceName));
        registrationBean.setOrder(Ordered.LOWEST_PRECEDENCE);
        return registrationBean;
    }
}
