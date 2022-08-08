package com.samuksa.user.service.user;

import com.samuksa.user.entity.db.jwt.CustomUserDetails;
import com.samuksa.user.entity.errorHandler.jwt.CustomJwtException;
import com.samuksa.user.entity.errorHandler.jwt.JwtErrorCode;
import com.samuksa.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private  final UserMapper userMapper;
    @Transactional
    public void joinuser(CustomUserDetails customUserDetails){
        customUserDetails.setUserAuth("ROLE_USER");

        userMapper.saveUser(customUserDetails);
    }

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException{
        CustomUserDetails customUserDetails = userMapper.getUserAccount(userId);
        if (customUserDetails == null)
            throw new CustomJwtException("아이디가 없습니다", JwtErrorCode.ID_REGISTERED);
        return customUserDetails;
    }

}
