package com.samuksa.user.db.table.samuksa_user_db.repository;

import com.samuksa.user.db.table.samuksa_user_db.entity.AuthEmail;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.Column;

public interface AuthEmailRepository extends JpaRepository<AuthEmail,Long> {
    AuthEmail findByauthEmail(String email);
    AuthEmail findByauthKey(String key);
    AuthEmail findByauth(String auth);

    boolean existsByauthEmail(String email);
}
