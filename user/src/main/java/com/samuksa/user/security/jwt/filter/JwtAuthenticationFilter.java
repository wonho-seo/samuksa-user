package com.samuksa.user.security.jwt.filter;

import com.samuksa.user.db.table.samuksa_user_db.entity.CustUser;
import com.samuksa.user.db.table.samuksa_user_db.entity.UserJwtToken;
import com.samuksa.user.db.table.samuksa_user_db.repository.CustUserRepository;
import com.samuksa.user.db.table.samuksa_user_db.repository.UserJwtTokenRepository;
import com.samuksa.user.errorexception.entity.errorHandler.jwt.CustomJwtException;
import com.samuksa.user.errorexception.entity.errorHandler.jwt.JwtErrorCode;
import com.samuksa.user.security.jwt.provider.JwtTokenProvider;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Pattern;

@Configuration
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean {
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final CustUserRepository custUserRepository;
    private final UserJwtTokenRepository userJwtTokenRepository;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        String token = jwtTokenProvider.resolveToken((HttpServletRequest) request,"Authorization");
        Authentication authentication;
        // 유효한 토큰인지 확인합니다.
        if (token != null && Pattern.matches("^Bearer .*", token)) {
            token = token.substring(7);
            try {
                if (jwtTokenProvider.validateToken(token)) {
                    authentication = jwtTokenProvider.getAuthentication(token);
                    // SecurityContext 에 Authentication 객체를 저장합니다.
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
            catch (SignatureException | MalformedJwtException e){
                throw new CustomJwtException("NonInvalid Token", JwtErrorCode.INVALID_TOKEN);
            }
            catch (IllegalArgumentException e){
                throw new CustomJwtException("Signature", JwtErrorCode.INVALID_TOKEN);
            }
            catch (ExpiredJwtException e) { //access 토큰 만료에 따른 refresh token 검증
                token = jwtTokenProvider.resolveToken((HttpServletRequest) request, "Refresh-Authorization");

                if (token != null && Pattern.matches("^Bearer .*", token)) {
                    token = token.substring(7);

                    if (jwtTokenProvider.validateToken(token)) {
                        authentication = jwtTokenProvider.getAuthentication(token);

                        response.setContentType("application/json");
                        response.setCharacterEncoding("utf-8");

                        CustUser custUser = custUserRepository.findByuserId(authentication.getName()).orElseThrow(() -> new UsernameNotFoundException("not found"));
                        UserJwtToken userJwtToken = userJwtTokenRepository.findByCustUser_userId(authentication.getName()).orElse(UserJwtToken.builder()
                                .custUserIdx(custUser.getUserIdx())
                                .build());
                        userJwtToken.setUserJwtRefreshToken(jwtTokenProvider.createRefreshToken(authentication.getName(), authentication.getAuthorities()));
                        userJwtToken.setUserJwtAccessToken(jwtTokenProvider.createToken(authentication.getName(), authentication.getAuthorities()));

                        ((HttpServletResponse) response).addHeader("Refresh-Token", userJwtToken.getUserJwtRefreshToken());
                        ((HttpServletResponse) response).addHeader("Access-Token", userJwtToken.getUserJwtAccessToken());
                    }
                    throw e;
                }
            }

        }
        filterChain.doFilter(request, response);
    }

    private boolean isNotMatches(String password, String encodePassword) {
        return !passwordEncoder.matches(password, encodePassword);
    }
}
