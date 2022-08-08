package com.samuksa.user.service.user;

import com.samuksa.user.config.jwt.JwtTokenProvider;
import com.samuksa.user.dto.user.UserBasicInfo;
import com.samuksa.user.entity.db.jwt.CustomUserDetails;
import com.samuksa.user.entity.errorHandler.db.DbErrorCode;
import com.samuksa.user.entity.errorHandler.db.DbException;
import com.samuksa.user.entity.errorHandler.jwt.CustomJwtException;
import com.samuksa.user.entity.errorHandler.jwt.JwtErrorCode;
import com.samuksa.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserAthService {
    private final JwtTokenProvider jwtTokenProvider;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserMapper userMapper;
    private final UserService userService;

    public String getJwtToken(UserBasicInfo userBasicInfo){
        UserDetails userDetails = userService.loadUserByUsername(userBasicInfo.getUserId());

        if (bCryptPasswordEncoder.matches(userBasicInfo.getPassWd(), userDetails.getPassword()))
            return jwtTokenProvider.createToken(userDetails.getUsername(), userDetails.getAuthorities());
        throw new CustomJwtException("비밀번호가 틀립니다", JwtErrorCode.PW_NOT_MATCH);
    }

    public Boolean isHaveId(UserBasicInfo userBasicInfo){
        return userMapper.getUserId(userBasicInfo.getUserId()) != null;
    }
    public Boolean isHaveName(UserBasicInfo userBasicInfo){
        return userMapper.getUserNikName(userBasicInfo.getUserNikName()) != null;
    }

    public void userSignUp(UserBasicInfo userBasicInfo){
        if (isHaveId(userBasicInfo) || isHaveName(userBasicInfo))
            throw new DbException("회원이 이미 존재합니다", DbErrorCode.ID_REGISTERED);
        CustomUserDetails customUserDetails = CustomUserDetails.builder()
                .userId(userBasicInfo.getUserId())
                .userEmail(userBasicInfo.getUserEmail())
                .userPassword(bCryptPasswordEncoder.encode(userBasicInfo.getPassWd()))
                .userNikName(userBasicInfo.getUserNikName())
                .build();
        userService.joinuser(customUserDetails);
    }

    public UserBasicInfo getUserInfo() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        CustomUserDetails customUserDetails = userMapper.getUserAccount(userId);
        if (customUserDetails == null)
            throw new CustomJwtException("잘못된 접근",JwtErrorCode.INTER_SERVER_ERROR);
        return UserBasicInfo.builder()
                .userEmail(customUserDetails.getUserEmail())
                .userId(customUserDetails.getUserId())
                .userNikName(customUserDetails.getUserNikName())
                .build();
    }

    public void userDelete(){
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        userMapper.deleteUser(userId);
    }
}
