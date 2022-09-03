package com.samuksa.user.service.user;

import com.samuksa.user.config.jwt.JwtTokenProvider;
import com.samuksa.user.dto.email.auth.EmailAuth;
import com.samuksa.user.dto.user.UserBasicInfo;
import com.samuksa.user.dto.user.db.UserJwtToken;
import com.samuksa.user.dto.user.response.UserJwtTokenResponse;
import com.samuksa.user.entity.db.jwt.CustomUserDetails;
import com.samuksa.user.errorexception.entity.errorHandler.db.DbErrorCode;
import com.samuksa.user.errorexception.entity.errorHandler.db.DbException;
import com.samuksa.user.errorexception.entity.errorHandler.jwt.CustomJwtException;
import com.samuksa.user.errorexception.entity.errorHandler.jwt.JwtErrorCode;
import com.samuksa.user.errorexception.entity.errorHandler.signup.SignupErrorCode;
import com.samuksa.user.errorexception.entity.errorHandler.signup.SignupException;
import com.samuksa.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.mybatis.spring.MyBatisSystemException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserAthService {
    private final JwtTokenProvider jwtTokenProvider;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserMapper userMapper;
    private final UserService userService;

    public UserJwtTokenResponse getJwtToken(UserBasicInfo userBasicInfo){
        CustomUserDetails customUserDetails = userMapper.getUserAccount(userBasicInfo.getUserId());
        if (customUserDetails == null)
            throw new CustomJwtException("아이디가 없습니다", JwtErrorCode.ID_REGISTERED);
        if (bCryptPasswordEncoder.matches(userBasicInfo.getPassWd(), customUserDetails.getPassword())){
            UserJwtTokenResponse userJwtTokenResponse = UserJwtTokenResponse.builder()
                    .accessToken(jwtTokenProvider.createToken(customUserDetails.getUsername(), customUserDetails.getAuthorities()))
                    .refreshToken(jwtTokenProvider.createRefreshToken(customUserDetails.getUsername(), customUserDetails.getAuthorities()))
                    .build();
            UserJwtToken userJwtToken = userMapper.getJwtTokenByUserId(userBasicInfo.getUserId());
            try {
                if (userJwtToken == null)
                    userMapper.saveJwtToken(userJwtTokenResponse.getAccessToken(), userJwtTokenResponse.getRefreshToken(), userBasicInfo.getUserId());
                else
                    userMapper.changeJwtToken(userJwtToken.getRefreshToken(), userJwtTokenResponse.getRefreshToken(), userJwtTokenResponse.getAccessToken());
            }
            catch (MyBatisSystemException e){
                userMapper.deleteJwtToken(userBasicInfo.getUserId());
                throw new CustomJwtException("Error Try Again Login", JwtErrorCode.INVALID_TOKEN);
            }
            return userJwtTokenResponse;
        }
        throw new CustomJwtException("비밀번호가 틀립니다", JwtErrorCode.PW_NOT_MATCH);
    }

    public UserJwtTokenResponse changeJwtToken(String accessToken, String refreshToken){
        UserJwtToken userJwtToken = userMapper.getJwtToken(accessToken);
        if (userJwtToken == null || !userJwtToken.getAccessToken().equals(accessToken) || !userJwtToken.getRefreshToken().equals(refreshToken))
            throw new CustomJwtException("Invaild Token", JwtErrorCode.INVALID_TOKEN);
        CustomUserDetails customUserDetails = userMapper.getUserAccount(userJwtToken.getUserId());
        if (customUserDetails == null)
            throw new CustomJwtException("Invaild Token", JwtErrorCode.INVALID_TOKEN);
        UserJwtTokenResponse userJwtTokenResponse = UserJwtTokenResponse.builder()
                .accessToken(jwtTokenProvider.createToken(customUserDetails.getUsername(), customUserDetails.getAuthorities()))
                .refreshToken(jwtTokenProvider.createRefreshToken(customUserDetails.getUsername(), customUserDetails.getAuthorities()))
                .build();
        userMapper.changeJwtToken(refreshToken,userJwtTokenResponse.getRefreshToken(), userJwtToken.getAccessToken());
        return userJwtTokenResponse;
    }
    public Boolean isHaveId(UserBasicInfo userBasicInfo){
        return userMapper.getUserId(userBasicInfo.getUserId()) != null;
    }
    public Boolean isHaveName(UserBasicInfo userBasicInfo){
        return userMapper.getUserNikName(userBasicInfo.getUserNickName()) != null;
    }

    public void userSignUp(UserBasicInfo userBasicInfo){
        if (isHaveId(userBasicInfo) || isHaveName(userBasicInfo))
            throw new DbException("회원이 이미 존재합니다", DbErrorCode.ID_REGISTERED);
        EmailAuth emailAuth = userMapper.getEmailKey(userBasicInfo.getUserEmail());
        if (emailAuth == null || emailAuth.getAuth() == 0)
            throw new SignupException("이메일 인증하지않음", SignupErrorCode.DONT_HAVE_EMAIL);

        CustomUserDetails customUserDetails = CustomUserDetails.builder()
                .userId(userBasicInfo.getUserId())
                .userEmail(userBasicInfo.getUserEmail())
                .userPassword(bCryptPasswordEncoder.encode(userBasicInfo.getPassWd()))
                .userNickName(userBasicInfo.getUserNickName())
                .build();
        userService.joinuser(customUserDetails);
    }

    public UserBasicInfo getUserInfo() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        CustomUserDetails customUserDetails = userMapper.getUserAccount(userId);
        if (customUserDetails == null)
            throw new CustomJwtException("잘못된 접근",JwtErrorCode.ID_NOT_FOUND);
        return UserBasicInfo.builder()
                .userEmail(customUserDetails.getUserEmail())
                .userId(customUserDetails.getUserId())
                .userNickName(customUserDetails.getUserNickName())
                .build();
    }

    public void userLogOut(String accessToken){
        UserJwtToken userJwtToken = userMapper.getJwtToken(accessToken);
        if (userJwtToken == null)
            throw new CustomJwtException("Invild Token",JwtErrorCode.INVALID_TOKEN);
        userMapper.deleteJwtToken(accessToken);
    }
    public void userDelete(UserBasicInfo userBasicInfo){
        CustomUserDetails customUserDetails = userMapper.getUserAccount(userBasicInfo.getUserId());
        if (customUserDetails == null)
            throw new DbException("아이디가 없음", DbErrorCode.ID_REGISTERED);
        if (bCryptPasswordEncoder.matches(userBasicInfo.getPassWd(), customUserDetails.getPassword())){
            userMapper.deleteUser(userBasicInfo.getUserId());
        }
        else
            throw new DbException("PassWord Not Match",DbErrorCode.PW_NOT_MATCH);
    }
}
