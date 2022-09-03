package com.samuksa.user.userinfo.mapper;

import com.samuksa.user.entity.db.jwt.CustomUserDetails;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface InfoMapper {
    CustomUserDetails getUserAccount(String userId);

    void changePassWord(@Param("userId") String userId, @Param("newPassWord") String newPassWord);

    void changeNickName(@Param("userId") String userId, @Param("nickName") String nickName);
}
