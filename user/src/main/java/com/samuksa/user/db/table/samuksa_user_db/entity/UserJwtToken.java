package com.samuksa.user.db.table.samuksa_user_db.entity;

import lombok.*;
import org.springframework.stereotype.Service;

import javax.persistence.*;

@Getter
@Entity
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_jwt_token", schema = "samuksa_user_db")
public class UserJwtToken{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cust_user_idx")
    private long idx;
    @Column(name = "user_jwt_refresh_token")
    private String userJwtRefreshToken;
    @Column(name = "user_jwt_access_token")
    private String userJwtAccessToken;

    @OneToOne
    @MapsId("cust_user_idx")
    @JoinColumn(name = "cust_user_idx")
    private CustUser custUser;
}
