package com.samuksa.user.domain.login.dto.request;


import com.samuksa.user.annotation.anno.UserId;
import com.samuksa.user.annotation.anno.UserPassword;
import com.samuksa.user.domain.login.dto.annogroup.LoginAnnoGroup;
import com.samuksa.user.domain.login.dto.annogroup.ChangeTokenAnnoGroup;
import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {

    @UserId(groups = {LoginAnnoGroup.class})
    private String userId;

    @UserPassword(groups = {LoginAnnoGroup.class})
    private String password;

    @NotNull(groups = {ChangeTokenAnnoGroup.class})
    private String accessToken;

    @NotNull(groups = {ChangeTokenAnnoGroup.class})
    private String refreshToken;
}
