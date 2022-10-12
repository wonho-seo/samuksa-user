package com.samuksa.user.config.security.jwt;

import com.samuksa.user.errorexception.entity.errorHandler.jwt.JwtErrorCode;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        String e = (String)request.getAttribute("exception");
        JwtErrorCode jwtErrorCode;
        if (e == null){
            jwtErrorCode = JwtErrorCode.NON_TOKEN;
            response.setStatus(jwtErrorCode.getCode());
            response.getWriter().write(jwtErrorCode.getMessage());
            return;
        }
        if (e.equals(JwtErrorCode.TOKEN_TIME_OUT.getMessage())){
            jwtErrorCode = JwtErrorCode.TOKEN_TIME_OUT;
            response.setStatus(jwtErrorCode.getCode());
            response.getWriter().write(jwtErrorCode.getMessage());
            return;
        }
        if (e.equals(JwtErrorCode.INVALID_TOKEN.getMessage())){
            jwtErrorCode = JwtErrorCode.INVALID_TOKEN;
            response.setStatus(jwtErrorCode.getCode());
            response.getWriter().write(jwtErrorCode.getMessage());
            return;
        }
    }

}
