package com.samuksa.user.dto.user.db;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter
@Getter
public class UserJwtToken {
    private int jwtTokenIdx;
    private String refreshToken;
    private String accessToken;
    private String userId;
}
