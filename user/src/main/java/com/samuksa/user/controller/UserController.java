package com.samuksa.user.controller;

import com.samuksa.user.config.jwt.JwtTokenProvider;
import com.samuksa.user.dto.user.UserBasicInfo;
import com.samuksa.user.entity.db.jwt.CustomUserDetails;
import com.samuksa.user.entity.errorHandler.jwt.CustomJwtException;
import com.samuksa.user.entity.errorHandler.jwt.JwtErrorCode;
import com.samuksa.user.mapper.UserMapper;
import com.samuksa.user.service.user.UserAthService;
import com.samuksa.user.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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


    @GetMapping("/login")
    public String login(@RequestParam(name = "userId") String userId, @RequestParam(name = "passwd") String passWd){
        UserBasicInfo userBasicInfo = UserBasicInfo.builder()
                .userId(userId)
                .passWd(passWd)
                .build();
        return userAthService.getJwtToken(userBasicInfo);
    }

    @PostMapping("/signUp/existence-id")
    public boolean isHaveId(@RequestParam(name = "userId") String userId){
        return userMapper.getUserAccount(userId) != null;
    }

    @PostMapping("/signUp/existence-name")
    public boolean isHaveName(@RequestParam(name = "userName") String userName){
        return userMapper.getUserAccount(userName) != null;
    }
    @PostMapping("/signUp")
    public  String signUp(@RequestParam(name = "userId") String userId, @RequestParam(name = "passwd") String passwd
            , @RequestParam(name = "userEmail") String userEmail, @RequestParam(name = "userName") String userName){
        UserBasicInfo basicInfo = UserBasicInfo.builder()
                .userId(userId)
                .userEmail(userEmail)
                .userNikName(userName)
                .passWd(passwd)
                .build();
        userAthService.userSignUp(basicInfo);
        return "success";
    }

    @PostMapping("/test")
    public String test(){
        return "test 통과";
    }

    @PostMapping("/user-info")
    public UserBasicInfo getUserInfo() {
        return userAthService.getUserInfo();
    }

    @DeleteMapping("/user-info")
    public String userDelete() {
        userAthService.userDelete();
        return "success remove";
    }
    @PostMapping("/access-denied")
    public String accessDenied(){
        return "access_denied";
    }
}
