package com.samuksa.user.dto.user;

import lombok.*;

@Builder
@Setter
@Getter
public class UserBasicInfo {
    private int userIdx;
    private String userId;
    private String passWd;
    private String userNickName;
    private String userEmail;
}
