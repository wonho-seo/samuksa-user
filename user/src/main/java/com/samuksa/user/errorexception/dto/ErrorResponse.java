package com.samuksa.user.errorexception.dto;

import com.samuksa.user.errorexception.entity.errorHandler.db.DbErrorCode;
import com.samuksa.user.errorexception.entity.errorHandler.email.EmailErrorCode;
import com.samuksa.user.errorexception.entity.errorHandler.jwt.JwtErrorCode;
import com.samuksa.user.errorexception.entity.errorHandler.signup.SignupErrorCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.MethodArgumentNotValidException;

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
    public ErrorResponse(MethodArgumentNotValidException ex){
        this.code = 400;
        this.message = ex.getMessage();
    }
}
