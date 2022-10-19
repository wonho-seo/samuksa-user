package com.samuksa.user.db.table.samuksa_user_db.repository;

import com.samuksa.user.db.table.samuksa_user_db.entity.AuthEmail;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.Column;
import java.util.Optional;

public interface AuthEmailRepository extends JpaRepository<AuthEmail,Long> {
    Optional<AuthEmail> findByauthEmail(String email);
    Optional<AuthEmail> findByauthKey(String key);
    Optional<AuthEmail> findByauth(String auth);

    boolean existsByauthEmail(String email);
}
