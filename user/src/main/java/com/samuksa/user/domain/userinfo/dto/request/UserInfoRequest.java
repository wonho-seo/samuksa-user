package com.samuksa.user.domain.userinfo.dto.request;

import com.samuksa.user.annotation.anno.UserId;
import com.samuksa.user.annotation.anno.UserNickName;
import com.samuksa.user.annotation.anno.UserPassword;
import com.samuksa.user.domain.userinfo.dto.request.annogroup.ChangeInfoAnnoGroup;
import com.samuksa.user.domain.userinfo.dto.request.annogroup.UserDeleteAnnoGroup;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class UserInfoRequest {
    @UserId(groups = {UserDeleteAnnoGroup.class, ChangeInfoAnnoGroup.class})
    private String userId;

    @UserPassword(groups = {UserDeleteAnnoGroup.class, ChangeInfoAnnoGroup.class})
    private String password;

    @UserPassword(groups = {ChangeInfoAnnoGroup.class}, nullable = true)
    private String newPassword = null;

    @UserNickName(groups = {ChangeInfoAnnoGroup.class}, nullable = true)
    private String newNickName = null;
}
