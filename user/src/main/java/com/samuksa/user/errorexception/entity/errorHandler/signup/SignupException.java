package com.samuksa.user.errorexception.entity.errorHandler.signup;

import lombok.Getter;

@Getter
public class SignupException extends RuntimeException{
    private SignupErrorCode signupErrorCode;

    public SignupException(String message, SignupErrorCode signupErrorCode) {
        super(message);
        this.signupErrorCode = signupErrorCode;
    }
}
