package com.stride.tracking.commons.configuration.kafka;

import com.stride.tracking.commons.constants.CustomHeaders;
import com.stride.tracking.commons.utils.RequestUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Headers;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class KafkaProducer {
    private static final String[] FORWARD_HEADER_KEY = {
            CustomHeaders.X_AUTH_USER_ID,
            CustomHeaders.X_AUTH_USERNAME,
            CustomHeaders.X_AUTH_EMAIL,
            CustomHeaders.X_AUTH_PROVIDER,
            CustomHeaders.X_AUTH_USER_AUTHORITIES,
            CustomHeaders.X_REQUEST_ID
    };

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void send(String topic, Object message) {
        ProducerRecord<String, Object> producerRecord = new ProducerRecord<>(topic, null, message);
        Headers headers = producerRecord.headers();

        HttpServletRequest currentRequest = RequestUtils.getCurrentHttpRequest();
        if (currentRequest != null) {
            for (String key : FORWARD_HEADER_KEY) {
                Optional.ofNullable(currentRequest.getHeader(key))
                        .ifPresent(val -> headers.add(key, val.getBytes()));
            }
        }

        kafkaTemplate.send(producerRecord);
    }
}
