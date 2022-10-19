package com.samuksa.user.security.basic.service;

import com.samuksa.user.security.basic.entity.CustomUserDetails;
import com.samuksa.user.db.table.samuksa_user_db.entity.CustUser;
import com.samuksa.user.db.table.samuksa_user_db.repository.CustUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final CustUserRepository custUserRepository;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException{
        CustUser custUser = custUserRepository.findByuserId(userId).orElseThrow(()-> new UsernameNotFoundException("아이디가 없습니다"));
        return new CustomUserDetails(custUser);
    }

}
