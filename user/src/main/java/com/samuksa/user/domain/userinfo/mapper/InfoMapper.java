package com.samuksa.user.domain.userinfo.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface InfoMapper {

    void deleteUserInfo(@Param("userId") String userId);
    void changePassWord(@Param("userId") String userId, @Param("newPassWord") String newPassWord);

    void changeNickName(@Param("userId") String userId, @Param("nickName") String nickName);
}
