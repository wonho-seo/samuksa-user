package com.samuksa.user.userinfo.service;

import com.samuksa.user.config.jwt.JwtTokenProvider;
import com.samuksa.user.dto.user.UserBasicInfo;
import com.samuksa.user.entity.db.jwt.CustomUserDetails;
import com.samuksa.user.errorexception.entity.errorHandler.db.DbErrorCode;
import com.samuksa.user.errorexception.entity.errorHandler.db.DbException;
import com.samuksa.user.userinfo.dto.request.UserInfoChangeRequest;
import com.samuksa.user.userinfo.mapper.InfoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InfoService {

    private final InfoMapper infoMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public void changeuserInfo(UserInfoChangeRequest userInfoChangeRequest){
        CustomUserDetails customUserDetails = infoMapper.getUserAccount(userInfoChangeRequest.getUserId());
        if (customUserDetails == null)
            throw new DbException("No Id", DbErrorCode.ID_REGISTERED);
        if (!bCryptPasswordEncoder.matches(userInfoChangeRequest.getOldPassWord(), customUserDetails.getPassword()))
            throw new DbException("Not Match PassWord", DbErrorCode.PW_NOT_MATCH);
        if (userInfoChangeRequest.getNewPassWord() != null) {
            userInfoChangeRequest.setNewPassWord(bCryptPasswordEncoder.encode(userInfoChangeRequest.getNewPassWord()));
            infoMapper.changePassWord(userInfoChangeRequest.getUserId(), userInfoChangeRequest.getNewPassWord());
        }
        if (userInfoChangeRequest.getNewNickName() != null)
            infoMapper.changeNickName(userInfoChangeRequest.getUserId(), userInfoChangeRequest.getNewNickName());

    }

}
