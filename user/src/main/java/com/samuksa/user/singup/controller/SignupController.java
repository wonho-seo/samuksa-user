package com.samuksa.user.singup.controller;

import com.samuksa.user.dto.user.UserBasicInfo;
import com.samuksa.user.mapper.UserMapper;
import com.samuksa.user.service.user.UserAthService;
import com.samuksa.user.service.user.email.EmailService;
import com.samuksa.user.service.user.email.EmailServiceImpl;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/signup")
public class SignupController {
    private final UserMapper userMapper;
    private final UserAthService userAthService;
    private final EmailServiceImpl emailService;

    @GetMapping("/existence-id")
    @ApiOperation(value = "아이디 중복확인", notes = "회원가입시 아이디 중복확인")
    public ResponseEntity<Boolean> isHaveId(@RequestParam(name = "userId") String userId){
        return ResponseEntity.status(200).body(userMapper.getUserAccount(userId) != null);
    }

    @GetMapping("/existence-name")
    @ApiOperation(value = "이름 중복확인", notes = "회원가입시 이름 중복확인")
    public ResponseEntity<Boolean> isHaveName(@RequestParam(name = "userName") String userName){
        return  ResponseEntity.status(200).body(userMapper.getUserAccount(userName) != null);
    }
    @PostMapping("")
    @ApiOperation(value = "회원가입", notes = "회원가입시 필요한 내용, 이메일 인증을 먼저 진행하지않으면 실행되지않음")
    public  ResponseEntity<String> signUp(@RequestParam(name = "userId") String userId, @RequestParam(name = "passwd") String passwd
            , @RequestParam(name = "userEmail") String userEmail, @RequestParam(name = "userName") String userName){
        UserBasicInfo basicInfo = UserBasicInfo.builder()
                .userId(userId)
                .userEmail(userEmail)
                .userNickName(userName)
                .passWd(passwd)
                .build();
        userAthService.userSignUp(basicInfo);

        return  ResponseEntity.status(201).body("success");
    }
    @PostMapping("/message")
    @ApiOperation(value = "인증용 메일 입력", notes = "먼저 인증키를 받을 메일을 입력")
    public ResponseEntity<String> sendSigupMessage(@RequestParam(name = "userEmail") String userEmail) throws Exception {
        String message = emailService.sendSimpleMessage(userEmail);
        return ResponseEntity.status(201).body(message);
    }
    @PostMapping("/message-auth")
    @ApiOperation(value = "메일 인증", notes = "message후 인증")
    public ResponseEntity<String> getAuthMessage(@RequestParam(name = "userEmail") String userEmail, @RequestParam(name = "Key") String key){
        if (emailService.getEmailAuth(userEmail,key))
            return ResponseEntity.status(200).body("success");
        return  ResponseEntity.status(400).body("not match");
    }
}
