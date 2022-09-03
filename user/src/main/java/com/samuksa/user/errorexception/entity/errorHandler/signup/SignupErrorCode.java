package com.samuksa.user.errorexception.entity.errorHandler.signup;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SignupErrorCode {
    DONT_HAVE_EMAIL(400, "dont have Email"),
    NOT_AUTH_EMAIL(400,  "do not Authentication Email")
    ;

    private int code;
    private String message;
}
