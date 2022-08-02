package com.samuksa.user.mapper;

import com.samuksa.user.dto.CustUser;
import com.samuksa.user.dto.security.CustomUserDetails;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
public interface UserMapper {

    CustomUserDetails getUserAccount(String userId);
    void    saveUser(CustomUserDetails customUserDetails);
    void deleteUser(String userId);
}
