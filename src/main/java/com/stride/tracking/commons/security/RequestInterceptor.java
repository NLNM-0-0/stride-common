package com.stride.tracking.commons.security;

import com.stride.tracking.commons.constants.CustomHeaders;
import io.micrometer.common.lang.NonNullApi;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;

@NonNullApi
public class RequestInterceptor implements ClientHttpRequestInterceptor {
    @Override
    public ClientHttpResponse intercept(
            HttpRequest request,
            byte[] body,
            ClientHttpRequestExecution execution) throws IOException {
        HttpServletRequest currentRequest = null;
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null) {
            currentRequest = requestAttributes.getRequest();
        }

        request.getHeaders().add(CustomHeaders.X_AUTH_USER_ID,
                currentRequest.getHeader(CustomHeaders.X_AUTH_USER_ID));
        request.getHeaders().add(CustomHeaders.X_AUTH_USERNAME,
                currentRequest.getHeader(CustomHeaders.X_AUTH_USERNAME));
        request.getHeaders().add(CustomHeaders.X_AUTH_EMAIL,
                currentRequest.getHeader(CustomHeaders.X_AUTH_EMAIL));
        request.getHeaders().add(CustomHeaders.X_AUTH_PROVIDER,
                currentRequest.getHeader(CustomHeaders.X_AUTH_PROVIDER));
        request.getHeaders().add(CustomHeaders.X_AUTH_USER_AUTHORITIES,
                currentRequest.getHeader(CustomHeaders.X_AUTH_USER_AUTHORITIES));

        request.getHeaders().add(CustomHeaders.X_REQUEST_ID,
                currentRequest.getHeader(CustomHeaders.X_REQUEST_ID));

        return execution.execute(request, body);
    }
}
