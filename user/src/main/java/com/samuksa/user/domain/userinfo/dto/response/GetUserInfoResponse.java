package com.samuksa.user.domain.userinfo.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetUserInfoResponse {
    private String userId;
    private String email;
    private String nickName;
    private byte[] profileImage;
}
