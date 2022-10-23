package com.samuksa.user.security.exception;

import com.samuksa.user.db.table.samuksa_user_db.entity.UserJwtToken;
import com.samuksa.user.db.table.samuksa_user_db.repository.UserJwtTokenRepository;
import com.samuksa.user.errorexception.entity.errorHandler.jwt.CustomJwtException;
import com.samuksa.user.security.basic.service.UserService;
import com.samuksa.user.security.jwt.provider.JwtTokenProvider;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
@RequiredArgsConstructor
public class SecurityExceptionFilter extends OncePerRequestFilter {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserJwtTokenRepository userJwtTokenRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try{
            filterChain.doFilter(request,response);
        }
        catch (CustomJwtException e){
            setErrorResponse(response, e.getJwtErrorCode().getMessage(), e.getJwtErrorCode().getCode());
        }
        catch (ExpiredJwtException e){
            setErrorResponse(response, "get Token", 300);
        }
    }

    private void setErrorResponse(HttpServletResponse response, String message, int status) throws IOException{
        response.setStatus(status);
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");

        response.getWriter().write(message);
    }
}
