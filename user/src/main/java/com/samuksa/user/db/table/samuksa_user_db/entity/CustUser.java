package com.samuksa.user.db.table.samuksa_user_db.entity;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.Collections;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "cust_user", schema = "samuksa_user_db")
public class CustUser{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idx")
    private long userIdx;
    @Column(name = "user_id")
    private String userId;
    @Column(name = "user_password")
    private String userPassword;
    @Column(name = "user_email")
    private String email;
    @Column(name = "user_nick_name")
    private String nickName;
    @Column(name = "user_auth")
    private String authentication;
    @Column(name = "user_profile_image_path")
    private String profileImagePath;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn(name = "idx", referencedColumnName = "cust_user_idx")
    private UserJwtToken userJwtToken;

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(this.authentication));
    }
}
