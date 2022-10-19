package com.samuksa.user.db.table.samuksa_user_db.repository;

import com.samuksa.user.db.table.samuksa_user_db.entity.UserJwtToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserJwtTokenRepository extends JpaRepository<UserJwtToken, Long> {
    Optional<UserJwtToken> findByCustUser_userId(String userId);
    Optional<UserJwtToken> findByuserJwtRefreshToken(String jwtRetreshToken);
    Optional<UserJwtToken> findByuserJwtAccessToken(String jwtAccessToken);

    boolean existsByuserJwtAccessToken(String jwtAccessToken);
}
