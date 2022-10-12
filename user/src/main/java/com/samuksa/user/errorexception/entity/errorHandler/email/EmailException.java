package com.samuksa.user.errorexception.entity.errorHandler.email;

import lombok.Getter;

@Getter
public class EmailException extends RuntimeException {
    private EmailErrorCode emailErrorCode;

    public EmailException(String message, EmailErrorCode emailErrorCode){
        super(message);
        this.emailErrorCode = emailErrorCode;
    }
}
