package com.stride.tracking.commons.utils;

import com.stride.tracking.commons.configuration.security.UserDetailsImpl;
import com.stride.tracking.commons.constants.CustomHeaders;
import com.stride.tracking.commons.constants.StrideConstants;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class HeaderUtils {
    private HeaderUtils() {
    }

    public static Map<String, String> getHeaders() {
        Map<String, String> headers = new HashMap<>();

        putAuthInfo(headers);

        putRequestId(headers);

        return headers;
    }

    private static void putAuthInfo(Map<String, String> headers) {
        Authentication authentication = SecurityUtils.getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

            headers.put(CustomHeaders.X_AUTH_USER_ID, userDetails.getId());
            headers.put(CustomHeaders.X_AUTH_EMAIL, userDetails.getEmail());
            headers.put(CustomHeaders.X_AUTH_PROVIDER, userDetails.getProvider());
            headers.put(CustomHeaders.X_AUTH_USERNAME, userDetails.getUsername());
            headers.put(
                    CustomHeaders.X_AUTH_USER_AUTHORITIES,
                    userDetails.getAuthorities().stream()
                            .map(GrantedAuthority::getAuthority)
                            .collect(Collectors.joining(StrideConstants.DELIMITER_COMMA))
            );
        }
    }

    private static void putRequestId(Map<String, String> headers) {
        HttpServletRequest request = RequestUtils.getCurrentHttpRequest();
        if (request != null) {
            String requestId = request.getHeader(CustomHeaders.X_REQUEST_ID);
            if (requestId != null) {
                headers.put(CustomHeaders.X_REQUEST_ID, requestId);
            }
        }
    }
}
