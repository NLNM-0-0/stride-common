package com.stride.tracking.commons.configuration.feign;

import com.stride.tracking.commons.utils.HeaderUtils;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class FeignRequestInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        Map<String, String> headers = HeaderUtils.getHeaders();
        headers.forEach(template::header);
    }
}
