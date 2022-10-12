package com.samuksa.user.db.table.samuksa_user_db.repository;

import com.samuksa.user.db.table.samuksa_user_db.entity.CustUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustUserRepository extends JpaRepository<CustUser,Long> {
    CustUser findByuserId(String userId);
    CustUser findByemail(String email);
    CustUser findBynickName(String nickName);
    CustUser findByauthentication(String authentication);
    CustUser findByname(String name);
    boolean existsByUserId(String userId);
    boolean existsByNickName(String nickName);
    boolean existsByEmail(String email);
    boolean existsByName(String name);
}
