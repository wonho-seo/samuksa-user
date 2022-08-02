package com.samuksa.user.service.user;

import com.samuksa.user.dto.CustUser;
import com.samuksa.user.dto.security.CustomUserDetails;
import com.samuksa.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.beans.Transient;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private  final UserMapper userMapper;

    @Transactional
    public void joinuser(CustomUserDetails customUserDetails){
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        customUserDetails.setUserPassword(bCryptPasswordEncoder.encode(customUserDetails.getPassword()));
        customUserDetails.setUserAuth("ROLE_USER");

        userMapper.saveUser(customUserDetails);
    }

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException{
        CustomUserDetails customUserDetails = userMapper.getUserAccount(userId);
        if (customUserDetails == null)
            throw new UsernameNotFoundException("not authorized");
        return customUserDetails;
    }

}
