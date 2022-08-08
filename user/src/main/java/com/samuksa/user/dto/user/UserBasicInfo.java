package com.samuksa.user.dto.user;

import lombok.*;

@Builder
@Setter
@Getter
public class UserBasicInfo {
    private String userId;
    private String passWd;
    private String userNikName;
    private String userEmail;
}
