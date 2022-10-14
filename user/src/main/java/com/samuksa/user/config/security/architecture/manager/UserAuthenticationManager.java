package com.samuksa.user.config.security.architecture.manager;

import com.samuksa.user.config.security.base.MyAuthenticationProvider;
import com.samuksa.user.config.security.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserAuthenticationManager implements AuthenticationManager {

    private final UserService userService;
    private final MyAuthenticationProvider myAuthenticationProvider;
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        return myAuthenticationProvider.authenticate(authentication);
    }
}
