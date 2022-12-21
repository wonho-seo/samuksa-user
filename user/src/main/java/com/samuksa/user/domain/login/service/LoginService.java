package com.samuksa.user.domain.login.service;


import com.samuksa.user.db.table.samuksa_user_db.entity.CustUser;
import com.samuksa.user.db.table.samuksa_user_db.entity.UserJwtToken;
import com.samuksa.user.db.table.samuksa_user_db.repository.CustUserRepository;
import com.samuksa.user.db.table.samuksa_user_db.repository.UserJwtTokenRepository;
import com.samuksa.user.security.jwt.provider.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class LoginService {

    private final UserJwtTokenRepository userJwtTokenRepository;
    private final CustUserRepository custUserRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public ResponseEntity<String> getJwtToken(OAuth2User oAuth2User){
        Map<String, String> m = oAuth2User.getAttribute("kakao_account");
        String email = m.get("email");
        CustUser custUser =custUserRepository.findByemail(email).orElseGet(() -> null);
        JSONObject jsonObject = new JSONObject();
        if (custUser == null){
            jsonObject.put("email", email);
            return ResponseEntity.status(400).body(jsonObject.toString());
        }
        UserJwtToken userJwtToken = getUserJwtTokenResponse(custUser);
        jsonObject.put("Access-Token", userJwtToken.getUserJwtAccessToken());
        return ResponseEntity.status(200).header("Refresh-Token", userJwtToken.getUserJwtRefreshToken())
                .body(jsonObject.toString());
    }

    public void logout(){
        UserJwtToken userJwtToken = userJwtTokenRepository.findByCustUser_userId(SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow(() -> new UsernameNotFoundException("not user found"));
        userJwtTokenRepository.delete(userJwtToken);
    }

    private UserJwtToken getUserJwtTokenResponse(CustUser custUser){
        UserJwtToken userJwtToken = UserJwtToken.builder()
                .custUser(custUser)
                .userJwtAccessToken(jwtTokenProvider.createToken(custUser.getUserId(), custUser.getAuthorities()))
                .userJwtRefreshToken(jwtTokenProvider.createRefreshToken(custUser.getUserId(), custUser.getAuthorities()))
                .build();
        if (userJwtTokenRepository.existsById(custUser.getUserIdx()))
            userJwtToken.setCustUser(custUser);
        userJwtTokenRepository.save(userJwtToken);
        return userJwtToken;
    }
}