package com.samuksa.user.errorexception.entity.errorHandler.signup;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SignupErrorCode {
    EXISTENCE_ID(400, "existence id"),
    EXISTENCE_EMAIL(400, "existence email"),
    EXISTENCE_NICKNAME(400, "existence nickname"),
    EXISTENCE_NO_VARIABLE(400, "no variable"),
    DO_NOT_HAVE_EMAIL(400, "dont have Email"),
    NOT_AUTH_EMAIL(400,  "do not Authentication Email")
    ;

    private int code;
    private String message;
}
