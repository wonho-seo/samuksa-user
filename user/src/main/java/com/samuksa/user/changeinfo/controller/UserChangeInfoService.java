package com.samuksa.user.changeinfo.controller;

import com.samuksa.user.changeinfo.dto.request.UserInfoChangeRequest;
import com.samuksa.user.dto.user.UserBasicInfo;
import com.samuksa.user.service.user.UserAthService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserChangeInfoService {

    private final UserAthService userAthService;

    @DeleteMapping("/user-info")
    @ApiOperation(value = "회원탈퇴", notes = "회원탈퇴, 아이디 패스워드 필요" )
    public ResponseEntity<String> userDelete(@RequestParam(name = "userId") String userId, @RequestParam(name = "passwd") String passWd) {
        UserBasicInfo userBasicInfo = UserBasicInfo.builder()
                .userId(userId)
                .passWd(passWd)
                .build();
        userAthService.userDelete(userBasicInfo);
        return  ResponseEntity.status(200).body("Success");
    }

    @GetMapping("/user-info")
    @ApiOperation(value = "유저정보", notes = "A-jwt토큰을 통한 유저정보" )
    public ResponseEntity<UserBasicInfo> getUserInfo() {

        return  ResponseEntity.status(200).body(userAthService.getUserInfo());
    }

//    @GetMapping("/user-info/a")
//    @ApiOperation(value = "회원정보수정", notes = "회원정보 수정" )
//    public ResponseEntity<UserInfoChangeRequest> changeInfo(final @Valid @RequestBody UserInfoChangeRequest userInfoChangeRequest) {
//        return ResponseEntity.status(200).body(userInfoChangeRequest);
//    }
}
