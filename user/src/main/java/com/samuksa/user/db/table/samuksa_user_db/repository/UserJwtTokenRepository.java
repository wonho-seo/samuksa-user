package com.samuksa.user.db.table.samuksa_user_db.repository;

import com.samuksa.user.db.table.samuksa_user_db.entity.UserJwtToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJwtTokenRepository extends JpaRepository<UserJwtToken, Long> {
    UserJwtToken findByCustUser_userId(String userId);
    UserJwtToken findByuserJwtRefreshToken(String jwtRetreshToken);
    UserJwtToken findByuserJwtAccessToken(String jwtAccessToken);

    boolean existsByuserJwtAccessToken(String jwtAccessToken);
}
