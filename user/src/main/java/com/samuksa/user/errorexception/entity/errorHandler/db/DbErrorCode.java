package com.samuksa.user.errorexception.entity.errorHandler.db;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum DbErrorCode {
    ID_NOT_FOUND(400,"ID NOT FOUND"),
    ID_REGISTERED(400, "ID REGISTERED"),
    PW_NOT_MATCH(400,"PASSWORD NOT MATCH"),
    ;

    private int code;
    private String message;
}
