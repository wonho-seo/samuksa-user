package com.samuksa.user.errorexception.entity.errorHandler.email;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum EmailErrorCode {
    Wrong_Approach(400, "Wrong_Approach"),
    Fail_Make_Message(500, "Fail Make Message"),
    ;

    private int code;
    private String message;
}
