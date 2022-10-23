package com.samuksa.user.domain.userinfo.service;

import com.samuksa.user.db.table.samuksa_user_db.entity.CustUser;
import com.samuksa.user.db.table.samuksa_user_db.repository.CustUserRepository;
import com.samuksa.user.db.table.samuksa_user_db.repository.UserJwtTokenRepository;
import com.samuksa.user.domain.userinfo.dto.request.UserInfoRequest;
import com.samuksa.user.domain.userinfo.dto.response.GetUserInfoResponse;
import com.samuksa.user.domain.userinfo.mapper.InfoMapper;
import com.samuksa.user.errorexception.entity.errorHandler.db.DbErrorCode;
import com.samuksa.user.errorexception.entity.errorHandler.db.DbException;
import lombok.RequiredArgsConstructor;
import org.aspectj.util.FileUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
public class InfoService {

    private final InfoMapper infoMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final CustUserRepository custUserRepository;


    public void deleteUserInfo(UserInfoRequest userInfoRequest){
        CustUser custUser = custUserRepository.findByuserId(userInfoRequest.getUserId()).orElseThrow(() -> new UsernameNotFoundException("not found user"));
        checkValidUser(userInfoRequest, custUser);
        infoMapper.deleteUserInfo(custUser.getUserId());
    }

    public GetUserInfoResponse getUserInfo(){
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        CustUser custUser = custUserRepository.findByuserId(userId).orElseThrow(() -> new UsernameNotFoundException("not found user"));
        File file = new File(custUser.getProfileImagePath());

        return GetUserInfoResponse.builder()
                .userId(custUser.getUserId())
                .email(custUser.getEmail())
                .nickName(custUser.getNickName())
                .profileImage(custUser.getProfileImagePath())
                .build();

    }

    public ResponseEntity<String> patchUserInfo(UserInfoRequest userInfoRequest){
        CustUser custUser = custUserRepository.findByuserId(SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow(() -> new UsernameNotFoundException("not found user"));
        if (!custUser.getUserId().equals(userInfoRequest.getUserId()) || custUser.getUserPassword().equals(bCryptPasswordEncoder.encode(userInfoRequest.getPassword())))
            return ResponseEntity.badRequest().body("nonValid user");
        if (userInfoRequest.getNewPassword() != null)
            custUser.setUserPassword(bCryptPasswordEncoder.encode(userInfoRequest.getNewPassword()));
        if (userInfoRequest.getNewNickName() != null)
            custUser.setNickName(userInfoRequest.getNewNickName());
        custUserRepository.save(custUser);
        return ResponseEntity.ok("success");
    }

    private void checkValidUser(UserInfoRequest userInfoRequest, CustUser custUser){
        if (custUser == null)
            throw new DbException("No Id", DbErrorCode.ID_REGISTERED);
        if (userInfoRequest != null && !bCryptPasswordEncoder.matches(userInfoRequest.getPassword(), custUser.getUserPassword()))
            throw new DbException("Not Match PassWord", DbErrorCode.PW_NOT_MATCH);
    }
}
