package com.samuksa.user.domain.login.controller;

import com.samuksa.user.domain.login.dto.annogroup.ChangeTokenAnnoGroup;
import com.samuksa.user.domain.login.dto.annogroup.LoginAnnoGroup;
import com.samuksa.user.domain.login.dto.annogroup.LogoutAnnoGroup;
import com.samuksa.user.domain.login.dto.request.LoginRequest;
import com.samuksa.user.domain.login.dto.response.UserJwtTokenResponse;
import com.samuksa.user.domain.login.service.LoginService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.json.JSONException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("/login")
@RequiredArgsConstructor
public class UserController {

    private final LoginService loginService;

    @PostMapping("/a")
    @ApiOperation(value = "로그인", notes = "id 와 password를 통한 jwt토큰 발급" )
    public ResponseEntity<UserJwtTokenResponse> login(final @RequestBody @Validated(LoginAnnoGroup.class) LoginRequest loginRequest){
        return ResponseEntity.status(201).body(loginService.getJwtToken(loginRequest));
    }

    @GetMapping("/oauth2")
    public ResponseEntity<String> kakaoLogin(@AuthenticationPrincipal OAuth2User oAuth2User) throws JSONException {
        return loginService.getJwtToken(oAuth2User);
    }

    @GetMapping("/test")
    public String test(){
        return "success";
    }

    @DeleteMapping("/jwt")
    @ApiOperation(value = "로그아웃", notes = "jwt토큰을 통한 로그아웃" )
    public ResponseEntity<String> logout(final @RequestBody @Validated(LogoutAnnoGroup.class) LoginRequest loginRequest){
        loginService.logout(loginRequest);
        return ResponseEntity.status(201).body("success");
    }

    @PostMapping("/refresh-token")
    @ApiOperation(value = "토큰 재발급", notes = "A-Token과 R-Token을 다시 재발급" )
    public ResponseEntity<UserJwtTokenResponse> changeToken(final @RequestBody @Validated(ChangeTokenAnnoGroup.class) LoginRequest loginRequest){
        return ResponseEntity.status(200).body(loginService.changeJwtToken(loginRequest));
    }

    @GetMapping("/oauth/loginInfo")
    @ResponseBody
    public String oauthLoginInfo(Authentication authentication, @AuthenticationPrincipal OAuth2User oAuth2UserPrincipal){
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        Map<String, Object> attributes = oAuth2User.getAttributes();

        Map<String, Object> attributes1 = oAuth2UserPrincipal.getAttributes();

        return attributes.toString();
    }
}
