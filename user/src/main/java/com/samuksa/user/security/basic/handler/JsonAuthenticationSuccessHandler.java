package com.samuksa.user.security.basic.handler;

import com.samuksa.user.db.table.samuksa_user_db.entity.CustUser;
import com.samuksa.user.db.table.samuksa_user_db.entity.UserJwtToken;
import com.samuksa.user.db.table.samuksa_user_db.repository.CustUserRepository;
import com.samuksa.user.db.table.samuksa_user_db.repository.UserJwtTokenRepository;
import com.samuksa.user.security.jwt.provider.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
@RequiredArgsConstructor
public class JsonAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private final CustUserRepository custUserRepository;
    private final UserJwtTokenRepository userJwtTokenRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        if (authentication.getPrincipal() == null) //anonymous 이면 토큰 미발급
            return;

        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");

        CustUser custUser = custUserRepository.findByuserId(authentication.getName()).orElseThrow(() -> new UsernameNotFoundException("not found"));
        UserJwtToken userJwtToken = userJwtTokenRepository.findByCustUser_userId(authentication.getName()).orElse(UserJwtToken.builder()
                        .custUserIdx(custUser.getUserIdx())
                        .build());
        userJwtToken.setUserJwtRefreshToken(jwtTokenProvider.createRefreshToken(authentication.getName(), authentication.getAuthorities()));
        userJwtToken.setUserJwtAccessToken(jwtTokenProvider.createToken(authentication.getName(), authentication.getAuthorities()));

        response.addHeader("Refresh-Token", userJwtToken.getUserJwtRefreshToken());
        response.addHeader("Access-Token", userJwtToken.getUserJwtAccessToken());
        userJwtTokenRepository.save(userJwtToken);
    }
}
