package com.samuksa.user.security.exception;

import com.samuksa.user.errorexception.entity.errorHandler.jwt.CustomJwtException;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
@RequiredArgsConstructor
public class SecurityExceptionFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try{
            filterChain.doFilter(request,response);
        }
        catch (CustomJwtException e){
            setErrorResponse(response, e.getJwtErrorCode().getMessage(), e.getJwtErrorCode().getCode());
        }
        catch (ExpiredJwtException e){
            System.out.println("abdsafsgasfg");
            doFilter(request, response, filterChain);
        }
        catch (AuthenticationServiceException e){
        }
    }

    private void setErrorResponse(HttpServletResponse response, String message, int status) throws IOException{
        response.setStatus(status);
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");

        response.getWriter().write(message);
    }
}
