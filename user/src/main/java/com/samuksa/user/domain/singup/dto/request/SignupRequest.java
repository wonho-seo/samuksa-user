package com.samuksa.user.domain.singup.dto.request;

import com.samuksa.user.annotation.anno.UserId;
import com.samuksa.user.annotation.anno.UserNickName;
import com.samuksa.user.annotation.anno.UserPassword;
import com.samuksa.user.domain.singup.dto.request.annogroup.AuthMessageAnnoGroup;
import com.samuksa.user.domain.singup.dto.request.annogroup.SingupAnnoGroup;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Getter
@Setter

public class SignupRequest {
    @UserId(groups = {SingupAnnoGroup.class})
    private String userId;

    @UserPassword(groups = {SingupAnnoGroup.class})
    private String password;

    @NotNull(groups = {SingupAnnoGroup.class, AuthMessageAnnoGroup.class})
    @Email(groups = {SingupAnnoGroup.class, AuthMessageAnnoGroup.class})
    private String email;

    @UserNickName(groups = {SingupAnnoGroup.class})
    private String nickName;

    private String authKey;
}