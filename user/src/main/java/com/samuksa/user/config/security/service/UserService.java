package com.samuksa.user.config.security.service;

import com.samuksa.user.config.security.entity.CustomUserDetails;
import com.samuksa.user.db.table.samuksa_user_db.entity.CustUser;
import com.samuksa.user.db.table.samuksa_user_db.entity.UserJwtToken;
import com.samuksa.user.db.table.samuksa_user_db.repository.CustUserRepository;
import com.samuksa.user.db.table.samuksa_user_db.repository.UserJwtTokenRepository;
import com.samuksa.user.errorexception.entity.errorHandler.jwt.CustomJwtException;
import com.samuksa.user.errorexception.entity.errorHandler.jwt.JwtErrorCode;
import com.samuksa.user.config.security.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final CustUserRepository custUserRepository;
    private  final UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException{
        CustUser custUser = custUserRepository.findByuserId(userId);
        if (custUser == null)
            throw new CustomJwtException("아이디가 없습니다", JwtErrorCode.ID_REGISTERED);
        return new CustomUserDetails(custUser);
    }

}
