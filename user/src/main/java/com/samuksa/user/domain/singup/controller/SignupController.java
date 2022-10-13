package com.samuksa.user.domain.singup.controller;

import com.samuksa.user.domain.singup.dto.request.SignupRequest;
import com.samuksa.user.domain.singup.dto.request.annogroup.AuthMessageAnnoGroup;
import com.samuksa.user.domain.singup.dto.request.annogroup.SingupAnnoGroup;
import com.samuksa.user.domain.singup.service.UserSignupServiceImpl;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/signup")
public class SignupController {
    private final UserSignupServiceImpl userSignupService;

    @GetMapping("/existence-info")
    @ApiOperation(value = "중복확인", notes = "회원가입시 아이디, 이메일, 닉네임 중복확인 셋중 하나는 있어야함")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "userId",
                    paramType = "body",
                    dataType = "string"
            ),
            @ApiImplicitParam(
                    name = "email",
                    paramType = "body",
                    dataType = "string"
            ),
            @ApiImplicitParam(
                    name = "nickName",
                    paramType = "body",
                    dataType = "string"
            )
    })
    public ResponseEntity<String> existenceInfo(final @ApiIgnore SignupRequest signupRequest){
        userSignupService.existence(signupRequest);
        return ResponseEntity.status(200).body("success");
    }

    @PostMapping
    @ApiOperation(value = "회원가입", notes = "회원가입시 필요한 내용, 이메일 인증을 먼저 진행하지않으면 실행되지않음")
    @ApiParam()
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "userId",
                    required = true,
                    paramType = "body",
                    dataType = "string"
            ),
            @ApiImplicitParam(
                    name = "password",
                    required = true,
                    paramType = "body",
                    dataType = "string"
            ),
            @ApiImplicitParam(
                    name = "email",
                    required = true,
                    paramType = "body",
                    dataType = "string"
            ),
            @ApiImplicitParam(
                    name = "nickName",
                    required = true,
                    paramType = "body",
                    dataType = "string"
            )
    })
    public  ResponseEntity<String> signUp(final @ApiIgnore @RequestBody @Validated(SingupAnnoGroup.class) SignupRequest signupRequest){
        userSignupService.signup(signupRequest);
        return  ResponseEntity.status(201).body("success");
    }
    @PostMapping("/message")
    @ApiOperation(value = "인증용 메일 입력", notes = "메일만 입력시 인증번호 발급 메일과 key발급시 인증")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "email",
                    required = true,
                    paramType = "body",
                    dataType = "string"
            ),
            @ApiImplicitParam(
                    name = "authKey",
                    paramType = "body",
                    dataType = "string"
            )
    })
    public ResponseEntity<String> authMessage(final @ApiIgnore @RequestBody @Validated(AuthMessageAnnoGroup.class) SignupRequest signupRequest) throws Exception {
        return userSignupService.authEmail(signupRequest);
    }

}
