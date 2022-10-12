package com.samuksa.user.domain.userinfo.controller;

import com.samuksa.user.domain.userinfo.dto.request.UserInfoRequest;
import com.samuksa.user.domain.userinfo.dto.request.annogroup.ChangeInfoAnnoGroup;
import com.samuksa.user.domain.userinfo.dto.request.annogroup.UserDeleteAnnoGroup;
import com.samuksa.user.domain.userinfo.dto.response.GetUserInfoResponse;
import com.samuksa.user.domain.userinfo.service.InfoService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.file.ConfigurationSource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.Multipart;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserInfoController {

    private final InfoService infoService;

    @DeleteMapping("/user-info")
    @ApiOperation(value = "회원탈퇴", notes = "회원탈퇴, 아이디 패스워드 필요" )
    public ResponseEntity<String> userDelete(final @RequestBody @Validated(UserDeleteAnnoGroup.class) UserInfoRequest userInfoRequest) {
        infoService.deleteUserInfo(userInfoRequest);
        return  ResponseEntity.status(200).body("Success");
    }

    @GetMapping("/user-info")
    @ApiOperation(value = "유저정보", notes = "A-jwt토큰을 통한 유저정보" )
    public ResponseEntity<GetUserInfoResponse> getUserInfo() {
        return  ResponseEntity.status(200).body(infoService.getUserInfo());
    }

    @PatchMapping("/user-info")
    @ApiOperation(value = "회원정보수정", notes = "회원정보 수정" )
    public ResponseEntity<String> changeInfo(final @RequestBody @Validated(ChangeInfoAnnoGroup.class) UserInfoRequest userInfoRequest) {
        infoService.patchUserInfo(userInfoRequest);
        return ResponseEntity.status(200).body("success");
    }

    @PostMapping("/upload-image")
    public ResponseEntity<String> uploadImage(final @RequestParam List<MultipartFile> image){
        return infoService.uploadImage(image);
    }

}
