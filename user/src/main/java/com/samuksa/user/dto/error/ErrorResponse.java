package com.samuksa.user.dto.error;

import com.samuksa.user.entity.errorHandler.db.DbErrorCode;
import com.samuksa.user.entity.errorHandler.email.EmailErrorCode;
import com.samuksa.user.entity.errorHandler.email.EmailException;
import com.samuksa.user.entity.errorHandler.jwt.JwtErrorCode;
import com.samuksa.user.entity.errorHandler.signup.SignupErrorCode;
import com.samuksa.user.entity.errorHandler.signup.SignupException;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorResponse {
    private int code;
    private String message;

    public ErrorResponse(JwtErrorCode jwtErrorCode){
        this.code = jwtErrorCode.getCode();
        this.message = jwtErrorCode.getMessage();
    }
    public ErrorResponse(DbErrorCode dbErrorCode){
        this.code = dbErrorCode.getCode();
        this.message = dbErrorCode.getMessage();
    }

    public ErrorResponse(EmailErrorCode emailErrorCode){
        this.code = emailErrorCode.getCode();
        this.message = emailErrorCode.getMessage();
    }

    public ErrorResponse(SignupErrorCode errorCode){
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
    }
}
