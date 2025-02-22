package com.hogimn.myanimechart.security.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.Base64;

@RestController
@RequestMapping("/oauth2")
@Slf4j
public class OAuth2Controller {
    private final String clientId;
    private final String clientSecret;
    private final String redirectUri;
    private final RestTemplate restTemplate;

    public OAuth2Controller(
            @Value("${myanimelist.client-id}")
            String clientId,
            @Value("${myanimelist.client-secret}")
            String clientSecret,
            @Value("${myanimelist.redirect-uri}")
            String redirectUri,
            RestTemplate restTemplate
    ) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.redirectUri = redirectUri;
        this.restTemplate = restTemplate;
    }

    @GetMapping("/callback/myanimelist")
    public ResponseEntity<?> callback(@RequestParam("code") String code, HttpSession session) {
        String codeVerifier = (String) session.getAttribute("code_verifier");
        log.info("Received code_verifier: {}", codeVerifier);
        log.info("Received code: {}", code);

        TokenResponse token = exchangeCodeForToken(code, codeVerifier);
        log.info("{}", token);

        return ResponseEntity.ok("로그인 성공");
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
        SecureRandom random = new SecureRandom();

        for (int i = 0; i < 100; i++) {
            int randomIndex = random.nextInt(characters.length());
            result.append(characters.charAt(randomIndex));
        }

        return result.toString();
    }
}
