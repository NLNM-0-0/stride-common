package com.stride.tracking.commons.mapper;

import com.mongodb.lang.Nullable;
import org.springframework.context.annotation.*;
import org.springframework.core.type.AnnotatedTypeMetadata;

@Configuration
public class MapperConfig {
    @Bean
    @Conditional(DataSourceCondition.class)
    public ReferenceMapper referenceMapper() {
        return new ReferenceMapper();
    }

    private static class DataSourceCondition implements Condition {
        @Override
        public boolean matches(ConditionContext context, @Nullable AnnotatedTypeMetadata metadata) {
            String dataSourceUrl = context.getEnvironment().getProperty("spring.datasource.url");
            String mongoUrl = context.getEnvironment().getProperty("spring.data.mongo.uri");
            return dataSourceUrl != null && mongoUrl != null;
        }
    }
}
