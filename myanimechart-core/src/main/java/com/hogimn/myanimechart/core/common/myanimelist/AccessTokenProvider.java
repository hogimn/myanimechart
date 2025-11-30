package com.hogimn.myanimechart.core.common.myanimelist;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.util.Optional;

@Component
@RequestScope
public class AccessTokenProvider {
    private final HttpServletRequest request;

    public AccessTokenProvider(HttpServletRequest request) {
        this.request = request;
    }

    public Optional<String> getAccessTokenFromCookie() {
        if (request.getCookies() != null) {
            for (var cookie : request.getCookies()) {
                if ("access_token".equals(cookie.getName()) && !cookie.getValue().isEmpty()) {
                    return Optional.of("Bearer " + cookie.getValue());
                }
            }
        }
        return Optional.empty();
    }
}