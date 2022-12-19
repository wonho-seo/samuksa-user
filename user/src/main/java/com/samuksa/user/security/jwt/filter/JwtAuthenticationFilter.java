package com.samuksa.user.security.jwt.filter;

import com.samuksa.user.db.table.samuksa_user_db.entity.CustUser;
import com.samuksa.user.db.table.samuksa_user_db.entity.UserJwtToken;
import com.samuksa.user.db.table.samuksa_user_db.repository.CustUserRepository;
import com.samuksa.user.db.table.samuksa_user_db.repository.UserJwtTokenRepository;
import com.samuksa.user.errorexception.entity.errorHandler.jwt.CustomJwtException;
import com.samuksa.user.errorexception.entity.errorHandler.jwt.JwtErrorCode;
import com.samuksa.user.security.jwt.provider.JwtTokenProvider;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Pattern;

@Configuration
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final CustUserRepository custUserRepository;
    private final UserJwtTokenRepository userJwtTokenRepository;


    private boolean isNotMatches(String password, String encodePassword) {
        return !passwordEncoder.matches(password, encodePassword);
    }

    private Cookie getCookie(HttpServletRequest request, String name){
        for (Cookie cookie : request.getCookies()){
            if (cookie.getName().equals(name)) {
                return cookie;
            }
        }
        return null;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
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
                else {
                    throw new CustomJwtException("non valid Token", JwtErrorCode.INVALID_TOKEN);
                }
            }
            catch (SignatureException | MalformedJwtException e){
                throw new CustomJwtException("NonInvalid Token", JwtErrorCode.INVALID_TOKEN);
            }
            catch (IllegalArgumentException e){
                throw new CustomJwtException("Signature", JwtErrorCode.INVALID_TOKEN);
            }
            catch (ExpiredJwtException e) { //access 토큰 만료에 따른 refresh token 검증
                Cookie cookie = getCookie((HttpServletRequest) request, "Refresh-Token");
                String refreshToken = cookie.getValue();

                authentication = jwtTokenProvider.getAuthentication(token);
                if (jwtTokenProvider.validateRefreshToken(refreshToken)) {
                    SecurityContextHolder.getContext().setAuthentication(authentication);

                    CustUser custUser = custUserRepository.findByuserId(authentication.getName()).orElseThrow(() -> new UsernameNotFoundException("not found"));
                    UserJwtToken userJwtToken = userJwtTokenRepository.findByCustUser_userId(authentication.getName()).orElse(UserJwtToken.builder()
                            .custUserIdx(custUser.getUserIdx())
                            .build());
                    userJwtToken.setUserJwtRefreshToken(jwtTokenProvider.createRefreshToken(authentication.getName(), authentication.getAuthorities()));
                    userJwtToken.setUserJwtAccessToken(jwtTokenProvider.createToken(authentication.getName(), authentication.getAuthorities()));

                    userJwtTokenRepository.save(userJwtToken);

                    cookie.setValue(userJwtToken.getUserJwtRefreshToken());
                    cookie.setPath("/");
                    cookie.setMaxAge(14 * 24 * 60 * 60);

                    ((HttpServletResponse) response).addCookie(cookie);
                    ((HttpServletResponse) response).addHeader("Access-Token", userJwtToken.getUserJwtAccessToken());

                }
                else {
                    throw new CustomJwtException("non valid Refresh Token", JwtErrorCode.INVALID_TOKEN);
                }
            }

        }
        filterChain.doFilter(request, response);
    }
}
