package com.samuksa.user.db.table.samuksa_user_db.repository;

import com.samuksa.user.db.table.samuksa_user_db.entity.CustUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustUserRepository extends JpaRepository<CustUser,Long> {
    Optional<CustUser> findByuserId(String userId);
    Optional<CustUser> findByemail(String email);
    Optional<CustUser> findBynickName(String nickName);
    Optional<CustUser> findByauthentication(String authentication);
    boolean existsByUserId(String userId);
    boolean existsByNickName(String nickName);
    boolean existsByEmail(String email);
}
