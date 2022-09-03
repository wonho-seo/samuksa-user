package com.samuksa.user.controller;

import com.samuksa.user.config.jwt.JwtTokenProvider;
import com.samuksa.user.dto.user.UserBasicInfo;
import com.samuksa.user.dto.user.response.UserJwtTokenResponse;
import com.samuksa.user.mapper.UserMapper;
import com.samuksa.user.service.user.UserAthService;
import com.samuksa.user.service.user.UserService;
import com.samuksa.user.service.user.email.EmailServiceImpl;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/user")

public class UserController {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserService userService;

    @Autowired
    private UserAthService userAthService;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private EmailServiceImpl emailService;

    @PostMapping("/login")
    @ApiOperation(value = "로그인", notes = "id 와 password를 통한 jwt토큰 발급" )
    public ResponseEntity<UserJwtTokenResponse> login(@RequestParam(name = "userId") String userId, @RequestParam(name = "passwd") String passWd){
        UserBasicInfo userBasicInfo = UserBasicInfo.builder()
                .userId(userId)
                .passWd(passWd)
                .build();
        return ResponseEntity.status(201).body(userAthService.getJwtToken(userBasicInfo));
    }


    @DeleteMapping("/login")
    @ApiOperation(value = "로그아웃", notes = "jwt토큰을 통한 로그아웃" )
    public ResponseEntity<String> logout(@RequestParam(name = "A-Token") String accessToken){
        userAthService.userLogOut(accessToken);
        return ResponseEntity.status(201).body("success");
    }
    @PostMapping("/refresh-token")
    @ApiOperation(value = "토큰 재발급", notes = "A-Token과 R-Token을 다시 재발급" )
    public ResponseEntity<UserJwtTokenResponse> changeToken(@RequestParam(name = "A-Token") String accessToken, @RequestParam(name = "R-Token") String refreshToken){
        return ResponseEntity.status(200).body(userAthService.changeJwtToken(accessToken, refreshToken));
    }

}
