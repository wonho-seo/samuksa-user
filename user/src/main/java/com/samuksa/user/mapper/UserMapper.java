package com.samuksa.user.mapper;

import com.samuksa.user.dto.email.auth.EmailAuth;
import com.samuksa.user.dto.user.db.UserJwtToken;
import com.samuksa.user.entity.db.jwt.CustomUserDetails;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {

    CustomUserDetails getUserAccount(String userId);
    String getUserId(String userId);
    String getUserNikName(String userNikName);
    EmailAuth getEmailKey(String email);
    UserJwtToken getJwtToken(@Param("ATOKEN") String accessToken);
    UserJwtToken getJwtTokenByUserId(@Param("USER_ID") String userId);
    void    saveUser(CustomUserDetails customUserDetails);
    void deleteUser(String userId);
    void saveEmailKey(@Param("EMAIL") String email,@Param("KEY") String key);

    void changeJwtToken(@Param("ORTOKEN") String oldRefreshToken, @Param("NRTOKEN") String newRefreshToken, @Param("ATOKEN") String accessToken);
    void deleteJwtToken(@Param("ATOKEN") String accessToken);
    void saveJwtToken(@Param("ATOKEN") String accessToken, @Param("RTOKEN") String refreshToken, @Param("USER_ID") String userId);
    void changeEmailKey(@Param("EMAIL") String email,@Param("KEY") String key);
    void successAuthEmail(String email);
}
