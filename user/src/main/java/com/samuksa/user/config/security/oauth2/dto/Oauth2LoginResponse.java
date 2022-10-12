package com.samuksa.user.config.security.oauth2.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Oauth2LoginResponse {
    private String accessToken;
    private String refreshToken;
    private String email;
}
