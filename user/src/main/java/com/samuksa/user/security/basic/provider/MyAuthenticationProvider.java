package com.samuksa.user.security.basic.provider;

import com.samuksa.user.security.basic.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class MyAuthenticationProvider implements AuthenticationProvider {
        private final UserService userService;

        @Override
        public Authentication authenticate(Authentication authentication) throws AuthenticationException {
            String loginId = authentication.getName();
            String password = (String) authentication.getCredentials();
            UserDetails customUserDetails = userService.loadUserByUsername(loginId);
            if (isNotMatches(password, customUserDetails.getPassword())) {
                throw new BadCredentialsException(loginId);
            }
            return new UsernamePasswordAuthenticationToken(customUserDetails, customUserDetails.getPassword(), customUserDetails.getAuthorities());
        }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

    private boolean isNotMatches(String password, String encodePassword) {
        return !passwordEncoder().matches(password, encodePassword);
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
