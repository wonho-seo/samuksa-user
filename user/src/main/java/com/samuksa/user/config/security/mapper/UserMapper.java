package com.samuksa.user.config.security.mapper;

import com.samuksa.user.config.security.entity.CustomUserDetails;
import com.samuksa.user.db.table.samuksa_user_db.entity.UserJwtToken;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {
    CustomUserDetails getUserAccount(String userId);

}
