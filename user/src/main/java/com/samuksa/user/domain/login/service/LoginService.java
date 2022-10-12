package com.samuksa.user.domain.login.service;


import com.samuksa.user.config.security.jwt.JwtTokenProvider;
import com.samuksa.user.db.table.samuksa_user_db.entity.CustUser;
import com.samuksa.user.db.table.samuksa_user_db.entity.UserJwtToken;
import com.samuksa.user.db.table.samuksa_user_db.repository.CustUserRepository;
import com.samuksa.user.db.table.samuksa_user_db.repository.UserJwtTokenRepository;
import com.samuksa.user.domain.login.dto.request.LoginRequest;
import com.samuksa.user.domain.login.dto.response.UserJwtTokenResponse;
import com.samuksa.user.errorexception.entity.errorHandler.jwt.CustomJwtException;
import com.samuksa.user.errorexception.entity.errorHandler.jwt.JwtErrorCode;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class LoginService {

    private final UserJwtTokenRepository userJwtTokenRepository;
    private final CustUserRepository custUserRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public ResponseEntity<String> getJwtToken(OAuth2User oAuth2User){
        Map<String, String> m = oAuth2User.getAttribute("kakao_account");
        String email = m.get("email");
        CustUser custUser =custUserRepository.findByemail(email);
        JSONObject jsonObject = new JSONObject();
        if (custUser == null){
            jsonObject.put("email", email);
            return ResponseEntity.status(400).body(jsonObject.toString());
        }
        UserJwtToken userJwtToken = getUserJwtTokenResponse(custUser);
        jsonObject.put("AccessToken", userJwtToken.getUserJwtAccessToken());
        return ResponseEntity.status(200).header("RefreshToken", userJwtToken.getUserJwtRefreshToken())
                .body(jsonObject.toString());
    }

    public UserJwtTokenResponse getJwtToken(LoginRequest loginRequest){
        CustUser custUser = custUserRepository.findByuserId(loginRequest.getUserId());
        if (custUser == null)
            throw new CustomJwtException("아이디가 없습니다", JwtErrorCode.ID_REGISTERED);
        if (bCryptPasswordEncoder.matches(loginRequest.getPassword(), custUser.getUserPassword())){
            UserJwtToken userJwtToken = UserJwtToken.builder()
                    .custUser(custUser)
                    .userJwtAccessToken(jwtTokenProvider.createToken(custUser.getUserId(), custUser.getAuthorities()))
                    .userJwtRefreshToken(jwtTokenProvider.createRefreshToken(custUser.getUserId(), custUser.getAuthorities()))
                    .build();
            if (userJwtTokenRepository.existsById(custUser.getUserIdx()))
                userJwtToken.setIdx(custUser.getUserIdx());
            userJwtTokenRepository.save(userJwtToken);
            return UserJwtTokenResponse.builder()
                    .accessToken(userJwtToken.getUserJwtAccessToken())
                    .refreshToken(userJwtToken.getUserJwtRefreshToken())
                    .build();
        }
        throw new CustomJwtException("비밀번호가 틀립니다", JwtErrorCode.PW_NOT_MATCH);
    }

    public void logout(LoginRequest loginRequest){
        UserJwtToken userJwtToken = userJwtTokenRepository.findByuserJwtAccessToken(loginRequest.getAccessToken());
        if (userJwtToken == null)
            throw new CustomJwtException("Invild Token",JwtErrorCode.INVALID_TOKEN);
        userJwtTokenRepository.delete(userJwtToken);
    }

    public UserJwtTokenResponse changeJwtToken(LoginRequest loginRequest){
        UserJwtToken userJwtToken = userJwtTokenRepository.findByuserJwtAccessToken(loginRequest.getAccessToken());
        if (userJwtToken == null || !userJwtToken.getUserJwtAccessToken().equals(loginRequest.getAccessToken()) || !userJwtToken.getUserJwtRefreshToken().equals(loginRequest.getRefreshToken()))
            throw new CustomJwtException("Invaild Token", JwtErrorCode.INVALID_TOKEN);
        userJwtToken.setUserJwtAccessToken(jwtTokenProvider.createToken(userJwtToken.getCustUser().getUserId(), userJwtToken.getCustUser().getAuthorities()));
        userJwtToken.setUserJwtRefreshToken(jwtTokenProvider.createRefreshToken(userJwtToken.getCustUser().getUserId(), userJwtToken.getCustUser().getAuthorities()));
        userJwtTokenRepository.save(userJwtToken);
        return UserJwtTokenResponse.builder()
                .accessToken(userJwtToken.getUserJwtAccessToken())
                .refreshToken(userJwtToken.getUserJwtRefreshToken())
                .build();
    }

    private UserJwtToken getUserJwtTokenResponse(CustUser custUser){
        UserJwtToken userJwtToken = UserJwtToken.builder()
                .custUser(custUser)
                .userJwtAccessToken(jwtTokenProvider.createToken(custUser.getUserId(), custUser.getAuthorities()))
                .userJwtRefreshToken(jwtTokenProvider.createRefreshToken(custUser.getUserId(), custUser.getAuthorities()))
                .build();
        if (userJwtTokenRepository.existsById(custUser.getUserIdx()))
            userJwtToken.setIdx(custUser.getUserIdx());
        userJwtTokenRepository.save(userJwtToken);
        return userJwtToken;
    }
}