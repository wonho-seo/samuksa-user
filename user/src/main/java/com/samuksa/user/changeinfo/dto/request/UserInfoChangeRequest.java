package com.samuksa.user.changeinfo.dto.request;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserInfoChangeRequest {

    @NotNull
    private String userId;

    @NotNull
    private String oldPassWord;

    private String newPassWord = null;
    private String newUserName = null;
}
