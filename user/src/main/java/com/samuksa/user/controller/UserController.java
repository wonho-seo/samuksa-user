package com.samuksa.user.controller;

import com.samuksa.user.config.jwt.JwtTokenProvider;
import com.samuksa.user.dto.CustUser;
import com.samuksa.user.dto.security.CustomUserDetails;
import com.samuksa.user.mapper.UserMapper;
import com.samuksa.user.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletRequest;
import javax.sql.DataSource;
import java.security.Principal;
import java.sql.Connection;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")

public class UserController {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @GetMapping("/login")
    public String login(@RequestParam(name = "userId") String userId){
        UserDetails userDetails = userService.loadUserByUsername(userId);
        return jwtTokenProvider.createToken(userDetails.getUsername(),userDetails.getAuthorities());
    }

    @PostMapping("/signUp")
    public  String signUp(@RequestParam(name = "userId") String userId, @RequestParam(name = "passwd") String passwd
            , @RequestParam(name = "userEmail") String userEmail, @RequestParam(name = "userName") String userName){
        CustomUserDetails customUserDetailsDb = userMapper.getUserAccount(userId);
        if (customUserDetailsDb.getUserId().equals(userId))
            return "가입된 id";

        CustomUserDetails customUserDetails = new CustomUserDetails();
        customUserDetails.setUserId(userId);
        customUserDetails.setUserName(userName);
        customUserDetails.setUserPassword(passwd);
        customUserDetails.setUserEmail(userEmail);

        userService.joinuser(customUserDetails);
        return "welcome " + customUserDetails.getUserId();
    }

    @PostMapping("/test")
    public String test(){
        return "test 통과";
    }

    @PostMapping("/user_info")
    public CustomUserDetails getUserInfo() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        CustomUserDetails customUserDetails = userMapper.getUserAccount(userId);
        return customUserDetails;
    }

    @PostMapping("/user_delete")
    public String userDelete() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        userMapper.deleteUser(userId);
        return "delete " + userId;
    }
    @GetMapping("/access_denied")
    public String accessDenied(){
        return "access_denied";
    }
}
