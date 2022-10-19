package com.samuksa.user.domain.login.controller;

import com.samuksa.user.domain.login.dto.annogroup.ChangeTokenAnnoGroup;
import com.samuksa.user.domain.login.dto.annogroup.LoginAnnoGroup;
import com.samuksa.user.domain.login.dto.request.LoginRequest;
import com.samuksa.user.domain.login.dto.response.UserJwtTokenResponse;
import com.samuksa.user.domain.login.service.LoginService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.json.JSONException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;


@RestController
@RequestMapping("/login")
@RequiredArgsConstructor
public class UserController {

    private final LoginService loginService;

    @ApiIgnore
    @GetMapping("/oauth2")
    public ResponseEntity<String> kakaoLogin(@AuthenticationPrincipal OAuth2User oAuth2User) throws JSONException {
        return loginService.getJwtToken(oAuth2User);
    }

    @DeleteMapping("/jwt")
    @ApiOperation(value = "로그아웃", notes = "jwt토큰을 통한 로그아웃" )
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "accessToken",
                    required = true,
                    paramType = "body",
                    dataType = "string"
            )
    })
    public ResponseEntity<String> logout(){
        loginService.logout();
        return ResponseEntity.status(201).body("success");
    }

}
