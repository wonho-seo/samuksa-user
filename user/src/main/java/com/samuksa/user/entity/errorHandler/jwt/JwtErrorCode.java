package com.samuksa.user.entity.errorHandler.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
public enum JwtErrorCode {
    ID_NOT_FOUND(401,"ID NOT FOUND"),
    ID_REGISTERED(401, "ID REGISTERED"),
    PW_NOT_MATCH(401,"PASSWORD NOT MATCH"),

    INTER_SERVER_ERROR(500, "INTER_SERVER ERROR")
    ;

    private int code;
    private String message;
}
