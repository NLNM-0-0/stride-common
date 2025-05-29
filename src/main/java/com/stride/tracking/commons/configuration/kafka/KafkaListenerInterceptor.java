package com.stride.tracking.commons.configuration.kafka;

import com.stride.tracking.commons.constants.CustomHeaders;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Header;
import org.slf4j.MDC;
import org.springframework.kafka.listener.RecordInterceptor;
import org.springframework.lang.NonNull;

import java.nio.charset.StandardCharsets;

public class KafkaListenerInterceptor implements RecordInterceptor<Object, Object> {
    private static final String[] LOG_HEADER_KEYS = {
            CustomHeaders.X_USER_TIMEZONE,
            CustomHeaders.X_AUTH_USER_ID
    };

    @Override
    public void afterRecord(
            @NonNull ConsumerRecord<Object, Object> consumerRecord,
            @NonNull Consumer<Object, Object> consumer) {
        MDC.clear();
    }

    @Override
    public ConsumerRecord<Object, Object> intercept(
            @NonNull ConsumerRecord<Object, Object> consumerRecord,
            @NonNull Consumer<Object, Object> consumer) {
        for (String key : LOG_HEADER_KEYS) {
            Header header = consumerRecord.headers().lastHeader(key);
            if (header != null) {
                MDC.put(key, new String(header.value(), StandardCharsets.UTF_8));
            }
        }
        return consumerRecord;
    }
}
