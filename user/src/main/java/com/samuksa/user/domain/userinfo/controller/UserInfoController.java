package com.samuksa.user.domain.userinfo.controller;

import com.samuksa.user.domain.userinfo.dto.request.UserInfoRequest;
import com.samuksa.user.domain.userinfo.dto.request.annogroup.ChangeInfoAnnoGroup;
import com.samuksa.user.domain.userinfo.dto.request.annogroup.UserDeleteAnnoGroup;
import com.samuksa.user.domain.userinfo.dto.response.GetUserInfoResponse;
import com.samuksa.user.domain.userinfo.service.ImageService;
import com.samuksa.user.domain.userinfo.service.InfoService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.file.ConfigurationSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.mail.Multipart;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.net.MalformedURLException;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserInfoController {

    private final InfoService infoService;
    private final ImageService imageService;
    @DeleteMapping("/user-info")
    @ApiOperation(value = "회원탈퇴", notes = "회원탈퇴, 아이디 패스워드 필요" )
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
            )
    })
    public ResponseEntity<String> userDelete(final @ApiIgnore @RequestBody @Validated(UserDeleteAnnoGroup.class) UserInfoRequest userInfoRequest) {
        infoService.deleteUserInfo(userInfoRequest);
        return  ResponseEntity.status(200).body("Success");
    }

    @GetMapping("/user-info")
    @ApiOperation(value = "유저정보", notes = "A-jwt토큰을 통한 유저정보" )
    public ResponseEntity<GetUserInfoResponse> getUserInfo() {
        return  ResponseEntity.status(200).body(infoService.getUserInfo());
    }

    @PatchMapping("/user-info")
    @ApiOperation(value = "회원정보수정", notes = "회원정보 수정")
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
                    name = "newPassword",
                    paramType = "body",
                    dataType = "string"
            ),
            @ApiImplicitParam(
                    name = "newNickName",
                    paramType = "body",
                    dataType = "string"
            ),
    })
    public ResponseEntity<String> changeInfo(final @ApiIgnore @RequestBody @Validated(ChangeInfoAnnoGroup.class) UserInfoRequest userInfoRequest) {
        return infoService.patchUserInfo(userInfoRequest);
    }

    @PostMapping("/upload-image")
    @ApiOperation(value = "이미지 업로드")
    public ResponseEntity<String> uploadImage(final @RequestPart MultipartFile image){
        return imageService.uploadImage(image);
    }

    @GetMapping(value = "/images/{filename}", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> showImage(HttpServletResponse response, @PathVariable String filename) throws MalformedURLException {
        return imageService.showImage(filename);
    }
}
