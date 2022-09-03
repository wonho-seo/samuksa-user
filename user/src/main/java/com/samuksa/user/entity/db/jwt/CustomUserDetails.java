package com.samuksa.user.entity.db.jwt;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.persistence.Entity;
import java.util.Collection;
import java.util.Collections;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomUserDetails implements UserDetails{

    private int custUserIdx;
    private String userId;
    private String userPassword;
    private String userEmail;
    private String userNickName;
    private String userAuth;

    private boolean emailVerified;	//이메일 인증 여부
    private boolean locked;	//계정 잠김 여부
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(this.userAuth));
    }


    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public void setUserAuth(String userAuth) {
        this.userAuth = userAuth;

    }

    @Override
    public String getPassword() {
        return userPassword;
    }


    @Override
    public String getUsername() {
        return userId;
    }

    /**
     * 계정 만료 여부
     * true : 만료 안됨
     * false : 만료

     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 계정 잠김 여부
     * true : 잠기지 않음
     * false : 잠김

     */
    @Override
    public boolean isAccountNonLocked() {
        return locked;
    }

    /**
     * 비밀번호 만료 여부
     * true : 만료 안됨
     * false : 만료

     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }


    /**
     * 사용자 활성화 여부
     * ture : 활성화
     * false : 비활성화

     */
    @Override
    public boolean isEnabled() {
        //이메일이 인증되어 있고 계정이 잠겨있지 않으면 true
        return (emailVerified && !locked);
    }

}
