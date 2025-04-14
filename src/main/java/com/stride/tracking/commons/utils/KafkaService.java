package com.stride.tracking.commons.utils;

import com.stride.tracking.commons.constants.CustomHeaders;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Headers;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class KafkaService {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void send(String topic, Object message) {
        HttpServletRequest currentRequest = null;
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null) {
            currentRequest = requestAttributes.getRequest();
        }

        Optional<String> userId = Optional.ofNullable(currentRequest.getHeader(CustomHeaders.X_AUTH_USER_ID));
        Optional<String> username = Optional.ofNullable(currentRequest.getHeader(CustomHeaders.X_AUTH_USERNAME));
        Optional<String> email = Optional.ofNullable(currentRequest.getHeader(CustomHeaders.X_AUTH_EMAIL));
        Optional<String> provider = Optional.ofNullable(currentRequest.getHeader(CustomHeaders.X_AUTH_PROVIDER));
        Optional<String> role = Optional.ofNullable(currentRequest.getHeader(CustomHeaders.X_AUTH_USER_AUTHORITIES));
        Optional<String> requestId = Optional.ofNullable(currentRequest.getHeader(CustomHeaders.X_REQUEST_ID));

        ProducerRecord<String, Object> producerRecord = new ProducerRecord<>(topic, null, message);

        Headers headers = producerRecord.headers();
        userId.ifPresent(s -> headers.add(CustomHeaders.X_AUTH_USER_ID, s.getBytes()));
        username.ifPresent(s -> headers.add(CustomHeaders.X_AUTH_USERNAME, s.getBytes()));
        email.ifPresent(s -> headers.add(CustomHeaders.X_AUTH_EMAIL, s.getBytes()));
        provider.ifPresent(s -> headers.add(CustomHeaders.X_AUTH_PROVIDER, s.getBytes()));
        role.ifPresent(s -> headers.add(CustomHeaders.X_AUTH_USER_AUTHORITIES, s.getBytes()));
        requestId.ifPresent(s -> headers.add(CustomHeaders.X_REQUEST_ID, s.getBytes()));

        kafkaTemplate.send(producerRecord);
    }
}
