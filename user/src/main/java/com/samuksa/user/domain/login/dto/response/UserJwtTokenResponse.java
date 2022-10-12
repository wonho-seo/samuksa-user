package com.samuksa.user.domain.login.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter
@Getter
public class UserJwtTokenResponse {
    private String accessToken;
    private String refreshToken;
}
