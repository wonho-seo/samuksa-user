package com.samuksa.user.userinfo.dto.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class UserInfoChangeRequest {

    @NotNull
    @Size(min = 5, max = 20)
    private String userId;

    @NotNull
    @Size(min = 5, max = 20)
    private String oldPassWord;

    @Size(min = 5, max = 20)
    private String newPassWord = null;
    @Max(20)
    private String newNickName = null;
}
