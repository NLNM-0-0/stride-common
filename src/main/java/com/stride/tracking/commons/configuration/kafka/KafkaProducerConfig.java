package com.stride.tracking.commons.configuration.kafka;

import io.micrometer.tracing.Tracer;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaProducerConfig {
    @Bean
    KafkaTemplate<String, Object> kafkaTemplate(
            ProducerFactory<String, Object> producerFactory) {
        var template = new KafkaTemplate<>(producerFactory);
        template.setObservationEnabled(true);

        return template;
    }

    @Bean
    KafkaProducer kafkaProducer(Tracer tracer, KafkaTemplate<String, Object> kafkaTemplate) {
        return new KafkaProducer(tracer, kafkaTemplate);
    }
}
