package com.hogimn.myanimechart.security.controller;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TokenResponse {
    private String token_type;
    private int expires_in;
    private String access_token;
    private String refresh_token;
}
