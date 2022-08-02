package com.samuksa.user.dto;

import lombok.*;
import org.springframework.stereotype.Repository;

public class CustUser {

    private int custUserIdx;
    private String userId;
    private String userPassword;
    private String userEmail;
    private String userName;

    public CustUser(int custUserIdx, String userId, String userPassword, String userEmail, String userName) {
        this.custUserIdx = custUserIdx;
        this.userId = userId;
        this.userPassword = userPassword;
        this.userEmail = userEmail;
        this.userName = userName;
    }

    public CustUser() {
    }

    public void setCustUserIdx(int custUserIdx) {
        this.custUserIdx = custUserIdx;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getCustUserIdx() {
        return custUserIdx;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getUserName() {
        return userName;
    }
}
