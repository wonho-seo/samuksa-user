package com.samuksa.user.config.security.architecture.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.samuksa.user.config.security.jwt.JwtTokenProvider;
import com.samuksa.user.domain.login.dto.request.LoginRequest;
import com.samuksa.user.domain.login.dto.response.UserJwtTokenResponse;
import com.samuksa.user.domain.login.service.LoginService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StreamUtils;

import javax.servlet.ServletException;
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

    private final JwtTokenProvider jwtTokenProvider;


    @Autowired
    public JsonUsernamePasswordAuthenticationFilter(ObjectMapper objectMapper, AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider
    ) {
        super(DEFAULT_ANT_PATH_REQUEST_MATCHER, authenticationManager);
        this.objectMapper = objectMapper;
        this.jwtTokenProvider = jwtTokenProvider;
        super.setAuthenticationSuccessHandler(new AuthenticationSuccessHandler() {
            @Override
            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                response.setContentType("application/json");
                response.setCharacterEncoding("utf-8");

                response.addHeader("Refresh Toekn", jwtTokenProvider.createRefreshToken(authentication.getName(),authentication.getAuthorities()));
                response.addHeader("Access Token", jwtTokenProvider.createToken(authentication.getName(),authentication.getAuthorities()));

            }
        });
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
        if(username ==null || password == null){
            throw new AuthenticationServiceException("DATA IS MISS");
        }
        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);
        // Allow subclasses to set the "details" property
        setDetails(request, authRequest);
        return this.getAuthenticationManager().authenticate(authRequest);//getAuthenticationManager를 커스텀해줌
    }



    protected void setDetails(HttpServletRequest request, UsernamePasswordAuthenticationToken authRequest) {
        authRequest.setDetails(this.authenticationDetailsSource.buildDetails(request));
    }




    @Data
    private static class LoginDto{
        String userId;
        String password;
    }

}
