package com.hogimn.myanimechart.service.oauth2;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Optional;

@RestController
@RequestMapping("/oauth2")
@Slf4j
public class OAuth2Controller {
    private final String clientId;
    private final String clientSecret;
    private final String redirectUri;
    private final String homeUrl;
    private final RestTemplate restTemplate;
    private static final String ACCESS_TOKEN_COOKIE = "access_token";
    private static final String CODE_VERIFIER_COOKIE = "code_verifier";

    public OAuth2Controller(
            @Value("${myanimelist.client-id}") String clientId,
            @Value("${myanimelist.client-secret}") String clientSecret,
            @Value("${myanimelist.redirect-uri}") String redirectUri,
            @Value("${myanimechart.home-url}") String homeUrl,
            RestTemplate restTemplate) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.redirectUri = redirectUri;
        this.homeUrl = homeUrl;
        this.restTemplate = restTemplate;
    }

    @GetMapping("/status")
    public ResponseEntity<Boolean> isAuthenticated(HttpServletRequest request) {
        return ResponseEntity.ok(getCookieValue(request, ACCESS_TOKEN_COOKIE).isPresent());
    }

    @PostMapping("/logout")
    public ResponseEntity<Boolean> logout(HttpServletResponse response) {
        deleteCookie(response, ACCESS_TOKEN_COOKIE);
        return ResponseEntity.ok(true);
    }

    @GetMapping("/callback/myanimelist")
    public void callback(@RequestParam("code") String code, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String codeVerifier = getCookieValue(request, CODE_VERIFIER_COOKIE).orElse("");
        deleteCookie(response, CODE_VERIFIER_COOKIE);

        TokenResponse token = exchangeCodeForToken(code, codeVerifier);
        setCookie(response, ACCESS_TOKEN_COOKIE, token.getAccessToken(), token.getExpiresIn());

        response.sendRedirect(homeUrl);
    }

    @GetMapping("/authorize/myanimelist")
    public void authorize(HttpServletResponse response) throws IOException {
        String codeChallenge = generateCodeChallenge();
        setCookie(response, CODE_VERIFIER_COOKIE, codeChallenge, 30);

        String authorizationUrl = String.format(
                "https://myanimelist.net/v1/oauth2/authorize?" +
                        "response_type=code" +
                        "&client_id=%s" +
                        "&redirect_uri=%s" +
                        "&scope=write:users" +
                        "&code_challenge=%s" +
                        "&code_challenge_method=plain",
                clientId, redirectUri, codeChallenge);

        response.sendRedirect(authorizationUrl);
    }

    private TokenResponse exchangeCodeForToken(String code, String codeVerifier) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>() {{
            add("client_id", clientId);
            add("client_secret", clientSecret);
            add("grant_type", "authorization_code");
            add("code", code);
            add("redirect_uri", redirectUri);
            add("code_verifier", codeVerifier);
        }};

        ResponseEntity<TokenResponse> response = restTemplate.postForEntity(
                "https://myanimelist.net/v1/oauth2/token",
                new HttpEntity<>(params, new HttpHeaders()),
                TokenResponse.class);

        return response.getBody();
    }

    private Optional<String> getCookieValue(HttpServletRequest request, String name) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (name.equals(cookie.getName()) && cookie.getValue() != null && !cookie.getValue().isEmpty()) {
                    return Optional.of(cookie.getValue());
                }
            }
        }
        return Optional.empty();
    }

    private void setCookie(HttpServletResponse response, String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(maxAge);
        response.addCookie(cookie);
    }

    private void deleteCookie(HttpServletResponse response, String name) {
        setCookie(response, name, null, 0);
    }

    private String generateCodeChallenge() {
        SecureRandom random;
        try {
            random = SecureRandom.getInstanceStrong();
        } catch (NoSuchAlgorithmException e) {
            random = new SecureRandom();
        }
        return random.ints(100, 0, 62)
                .mapToObj("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"::charAt)
                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                .toString();
    }
}