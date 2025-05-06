package com.stride.tracking.commons.configuration.security;

import com.stride.tracking.commons.constants.CustomHeaders;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class SecurityFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        Optional<String> id = Optional.ofNullable(request.getHeader(CustomHeaders.X_AUTH_USER_ID));
        Optional<String> userName = Optional.ofNullable(request.getHeader(CustomHeaders.X_AUTH_USERNAME));
        Optional<String> email = Optional.ofNullable(request.getHeader(CustomHeaders.X_AUTH_EMAIL));
        Optional<String> provider = Optional.ofNullable(request.getHeader(CustomHeaders.X_AUTH_PROVIDER));
        Optional<String> authorities = Optional.ofNullable(request.getHeader(CustomHeaders.X_AUTH_USER_AUTHORITIES));

        // Username can be null here
        if (id.isPresent() && email.isPresent()
                && provider.isPresent() && authorities.isPresent()) {
            UserDetails userDetails = UserDetailsImpl.builder()
                    .id(id.get())
                    .userName(userName.orElse(null))
                    .email(email.get())
                    .provider(provider.get())
                    .password("PASSWORD")
                    .authorities(List.of(new SimpleGrantedAuthority(authorities.get())))
                    .build();

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities()
            );
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }
        filterChain.doFilter(request, response);
    }
}
