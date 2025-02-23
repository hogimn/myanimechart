package com.hogimn.myanimechart.security.oauth2.controller;

import com.hogimn.myanimechart.security.oauth2.dto.TokenResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
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
import java.util.Base64;

@RestController
@RequestMapping("/oauth2")
@Slf4j
public class OAuth2Controller {
    private final String clientId;
    private final String clientSecret;
    private final String redirectUri;
    private final String homeUrl;
    private final RestTemplate restTemplate;

    public OAuth2Controller(
            @Value("${myanimelist.client-id}")
            String clientId,
            @Value("${myanimelist.client-secret}")
            String clientSecret,
            @Value("${myanimelist.redirect-uri}")
            String redirectUri,
            @Value("${myanimechart.home-url}")
            String homeUrl,
            RestTemplate restTemplate
    ) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.redirectUri = redirectUri;
        this.homeUrl = homeUrl;
        this.restTemplate = restTemplate;
    }

    @GetMapping("/isAuthenticated")
    public ResponseEntity<Boolean> isAuthenticated(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("access_token".equals(cookie.getName())
                        && cookie.getValue() != null
                        && !cookie.getValue().isEmpty()) {
                    return ResponseEntity.ok(true);
                }
            }
        }
        return ResponseEntity.ok(false);
    }

    @PostMapping("/logout")
    public ResponseEntity<Boolean> logout(HttpSession session, HttpServletResponse response) {
        session.invalidate();

        Cookie cookie = new Cookie("access_token", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setDomain("localhost");
        response.addCookie(cookie);

        return ResponseEntity.ok(true);
    }

    @GetMapping("/callback/myanimelist")
    public void callback(
            @RequestParam("code") String code,
            HttpSession session,
            HttpServletResponse response
    ) throws IOException {
        log.info("Received code: {}", code);

        String codeVerifier = (String) session.getAttribute("code_verifier");
        log.info("Received code_verifier: {}", codeVerifier);

        TokenResponse token = exchangeCodeForToken(code, codeVerifier);
        log.info("Received token: {}", token);

        session.setAttribute("refresh_token", token.getRefreshToken());

        Cookie accessTokenCookie = new Cookie("access_token", token.getAccessToken());
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setSecure(true);
        accessTokenCookie.setPath("/");
        accessTokenCookie.setMaxAge(token.getExpiresIn());
        response.addCookie(accessTokenCookie);

        response.sendRedirect(homeUrl);
    }

    private TokenResponse exchangeCodeForToken(String code, String codeVerifier) {
        HttpHeaders headers = new HttpHeaders();

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("grant_type", "authorization_code");
        params.add("code", code);
        params.add("redirect_uri", redirectUri);
        params.add("code_verifier", codeVerifier);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        String tokenUrl = "https://myanimelist.net/v1/oauth2/token";
        ResponseEntity<TokenResponse> response = restTemplate.postForEntity(tokenUrl, request, TokenResponse.class);

        return response.getBody();
    }

    private String encodeClientCredentials() {
        String credentials = clientId + ":" + clientSecret;
        return Base64.getEncoder().encodeToString(credentials.getBytes());
    }

    @GetMapping("/authorize/myanimelist")
    public void authorize(HttpServletResponse response, HttpSession session) throws IOException {
        String codeChallenge = generateCodeChallenge();

        log.info("Generated code_verifier: {}", codeChallenge);
        session.setAttribute("code_verifier", codeChallenge);

        String responseType = "code";
        String scope = "write:users";

        String authorizationUrl = "https://myanimelist.net/v1/oauth2/authorize?" +
                "response_type=" + responseType +
                "&client_id=" + clientId +
                "&redirect_uri=" + redirectUri +
                "&scope=" + scope +
                "&code_challenge=" + codeChallenge +
                "&code_challenge_method=plain";

        response.sendRedirect(authorizationUrl);
    }

    public String generateCodeChallenge() {
        StringBuilder result = new StringBuilder();
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom random;
        try {
            random = SecureRandom.getInstanceStrong();
        } catch (NoSuchAlgorithmException e) {
            random = new SecureRandom();
        }

        for (int i = 0; i < 100; i++) {
            int randomIndex = random.nextInt(characters.length());
            result.append(characters.charAt(randomIndex));
        }

        return result.toString();
    }
}
