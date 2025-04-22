package com.stride.tracking.commons.utils;

import com.stride.tracking.commons.configuration.security.UserDetailsImpl;
import com.stride.tracking.commons.constants.CustomHeaders;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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

            Optional<? extends GrantedAuthority> firstAuthority = userDetails.getAuthorities().stream().findFirst();
            firstAuthority.ifPresent(auth ->
                    headers.put(CustomHeaders.X_AUTH_USER_AUTHORITIES, auth.getAuthority())
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
