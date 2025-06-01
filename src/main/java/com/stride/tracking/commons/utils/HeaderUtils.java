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

        putRequestHeader(headers);

        return headers;
    }

    private static void putAuthInfo(Map<String, String> headers) {
        Authentication authentication = SecurityUtils.getAuthentication();

        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return;
        }

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        headers.put("X-Auth-User-Id", userDetails.getId());
        headers.put("X-Auth-Email", userDetails.getEmail());
        headers.put("X-Auth-Provider", userDetails.getProvider());
        headers.put("X-Auth-Username", userDetails.getUsername());

        Optional<? extends GrantedAuthority> firstAuthority = userDetails.getAuthorities().stream().findFirst();
        firstAuthority.ifPresent(
                auth -> headers.put("X-Auth-User-Authorities", auth.getAuthority())
        );
    }

    private static void putRequestHeader(Map<String, String> headers) {
        HttpServletRequest request = RequestUtils.getCurrentHttpRequest();
        if (request != null) {
            String timeZone = request.getHeader(CustomHeaders.X_USER_TIMEZONE);
            if (timeZone != null) {
                headers.put(CustomHeaders.X_USER_TIMEZONE, timeZone);
            }
        }
    }
}
