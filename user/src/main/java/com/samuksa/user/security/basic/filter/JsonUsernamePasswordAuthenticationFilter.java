package com.samuksa.user.security.basic.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.samuksa.user.security.basic.handler.JsonAuthenticationSuccessHandler;
import com.samuksa.user.security.basic.manager.UserAuthenticationManager;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StreamUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Configuration
public class JsonUsernamePasswordAuthenticationFilter extends AbstractAuthenticationProcessingFilter {


    private final ObjectMapper objectMapper;

    public static final String SPRING_SECURITY_FORM_USERNAME_KEY = "userId";

    public static final String SPRING_SECURITY_FORM_PASSWORD_KEY = "password";

    public static final String HTTP_METHOD = "POST";

    private static final AntPathRequestMatcher DEFAULT_ANT_PATH_REQUEST_MATCHER = new AntPathRequestMatcher("/login",
            HTTP_METHOD);
    private final UserAuthenticationManager userAuthenticationManager;

    @Autowired
    public JsonUsernamePasswordAuthenticationFilter(ObjectMapper objectMapper, AuthenticationManager authenticationManager,
                                                    UserAuthenticationManager userAuthenticationManager, JsonAuthenticationSuccessHandler jsonAuthenticationSuccessHandler
    ) {
        super(DEFAULT_ANT_PATH_REQUEST_MATCHER, authenticationManager);
        this.objectMapper = objectMapper;
        this.userAuthenticationManager = userAuthenticationManager;
        super.setAuthenticationSuccessHandler(jsonAuthenticationSuccessHandler);
    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException {

        if (!request.getMethod().equals(HTTP_METHOD) || !request.getContentType().equals("application/json")) {//POST가 아니거나 JSON이 아닌 경우
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }

        LoginDto loginDto = objectMapper.readValue(StreamUtils.copyToString(request.getInputStream(), StandardCharsets.UTF_8), LoginDto.class);
        String username = loginDto.getUserId();
        String password = loginDto.getPassword();
        if (username == null || password == null) {
            throw new AuthenticationServiceException("DATA IS MISS");
        }
        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);
        // Allow subclasses to set the "details" property
        setDetails(request, authRequest);
        return userAuthenticationManager.authenticate(authRequest);//getAuthenticationManager를 커스텀해줌
    }


    protected void setDetails(HttpServletRequest request, UsernamePasswordAuthenticationToken authRequest) {
        authRequest.setDetails(this.authenticationDetailsSource.buildDetails(request));
    }

    @Data
    private static class LoginDto {
        String userId;
        String password;
    }

}
