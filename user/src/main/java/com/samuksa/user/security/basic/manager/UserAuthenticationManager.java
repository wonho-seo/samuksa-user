package com.samuksa.user.security.basic.manager;

import com.samuksa.user.security.basic.provider.MyAuthenticationProvider;
import com.samuksa.user.security.basic.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserAuthenticationManager implements AuthenticationManager {

    private final MyAuthenticationProvider myAuthenticationProvider;
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        return myAuthenticationProvider.authenticate(authentication);
    }
}
