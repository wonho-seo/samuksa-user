package com.samuksa.user.db.table.samuksa_user_db.repository;

import com.samuksa.user.db.table.samuksa_user_db.entity.CustUser;
import com.samuksa.user.db.table.samuksa_user_db.entity.UserJwtToken;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AuthEmailRepositoryTest {

    @Autowired
    private CustUserRepository custUserRepository;
    @Autowired
    private UserJwtTokenRepository userJwtTokenRepository;
    @Test
    void findByauthEmailEmail() {
        CustUser custUser = CustUser.builder()
                .userId("abc")
                .userPassword("bdf")
                .email("bdgba")
                .nickName("zgbrgrb")
                .auth("dgbdgf")
                .build();
        custUserRepository.save(custUser);
        custUser = custUserRepository.findByuserId("abc");
        UserJwtToken userJwtToken = UserJwtToken.builder()
                .userJwtAccessToken("fbnd")
                .userJwtRefreshToken("avnd")
                .custUser(custUser)
                .build();
        userJwtTokenRepository.save(userJwtToken);
        long a =2 ;
        UserJwtToken userJwtToken1 = userJwtTokenRepository.findByuserJwtAccessToken("fbnd");
        userJwtTokenRepository.delete(userJwtToken);
        CustUser custUser1 = custUserRepository.findByuserId("abc");

        assertEquals(custUser.getUserId(),custUser1.getUserId());
    }

    @Test
    void findByauthEmailKey() {
    }

    @Test
    void findByauthEmailAuth() {
    }
}