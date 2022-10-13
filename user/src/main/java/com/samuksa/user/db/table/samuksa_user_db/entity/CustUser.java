package com.samuksa.user.db.table.samuksa_user_db.entity;

import com.samuksa.user.annotation.anno.UserPassword;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

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
@Table(schema = "cust_user")
public class CustUser {
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

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "custUser")
    @PrimaryKeyJoinColumn
    private UserJwtToken userJwtToken;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "custUser")
    @PrimaryKeyJoinColumn
    private UserImage userImage;

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(this.authentication));
    }
}
