package com.samuksa.user.mapper;

import com.samuksa.user.entity.db.jwt.CustomUserDetails;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {

    CustomUserDetails getUserAccount(String userId);
    String getUserId(String userId);
    String getUserNikName(String userNikName);
    void    saveUser(CustomUserDetails customUserDetails);
    void deleteUser(String userId);
}
