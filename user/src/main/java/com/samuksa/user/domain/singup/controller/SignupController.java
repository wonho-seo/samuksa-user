package com.samuksa.user.domain.singup.controller;

import com.samuksa.user.domain.singup.dto.request.SignupRequest;
import com.samuksa.user.domain.singup.dto.request.annogroup.AuthMessageAnnoGroup;
import com.samuksa.user.domain.singup.dto.request.annogroup.SingupAnnoGroup;
import com.samuksa.user.domain.singup.service.UserSignupServiceImpl;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/signup")
public class SignupController {
    private final UserSignupServiceImpl userSignupService;

    @GetMapping("/existence-info")
    @ApiOperation(value = "중복확인", notes = "회원가입시 아이디 중복확인")
    public ResponseEntity<String> existenceInfo(final @RequestBody SignupRequest signupRequest){
        userSignupService.existence(signupRequest);
        return ResponseEntity.status(200).body("success");
    }

    @PostMapping
    @ApiOperation(value = "회원가입", notes = "회원가입시 필요한 내용, 이메일 인증을 먼저 진행하지않으면 실행되지않음")
    public  ResponseEntity<String> signUp(final @RequestBody @Validated(SingupAnnoGroup.class) SignupRequest signupRequest){
        userSignupService.signup(signupRequest);
        return  ResponseEntity.status(201).body("success");
    }
    @PostMapping("/message")
    @ApiOperation(value = "인증용 메일 입력", notes = "메일만 입력시 인증번호 발급 메일과 key발급시 인증")
    public ResponseEntity<String> authMessage(final @RequestBody @Validated(AuthMessageAnnoGroup.class) SignupRequest signupRequest) throws Exception {
        return userSignupService.authEmail(signupRequest);
    }

}
