package com.stride.tracking.commons.configuration.feign;

import com.stride.tracking.commons.constants.CustomHeaders;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.stereotype.Component;

@Component
public class FeignRequestInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null) {
            var currentRequest = requestAttributes.getRequest();

            template.header(CustomHeaders.X_AUTH_USER_ID, currentRequest.getHeader(CustomHeaders.X_AUTH_USER_ID));
            template.header(CustomHeaders.X_AUTH_USERNAME, currentRequest.getHeader(CustomHeaders.X_AUTH_USERNAME));
            template.header(CustomHeaders.X_AUTH_EMAIL, currentRequest.getHeader(CustomHeaders.X_AUTH_EMAIL));
            template.header(CustomHeaders.X_AUTH_PROVIDER, currentRequest.getHeader(CustomHeaders.X_AUTH_PROVIDER));
            template.header(CustomHeaders.X_AUTH_USER_AUTHORITIES, currentRequest.getHeader(CustomHeaders.X_AUTH_USER_AUTHORITIES));
            template.header(CustomHeaders.X_REQUEST_ID, currentRequest.getHeader(CustomHeaders.X_REQUEST_ID));
        }
    }
}
