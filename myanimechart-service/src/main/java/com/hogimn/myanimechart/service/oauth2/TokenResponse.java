package com.hogimn.myanimechart.service.oauth2;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TokenResponse(
        @JsonProperty("token_type")
        String tokenType,

        @JsonProperty("expires_in")
        int expiresIn,

        @JsonProperty("access_token")
        String accessToken,

        @JsonProperty("refresh_token")
        String refreshToken
) {
}