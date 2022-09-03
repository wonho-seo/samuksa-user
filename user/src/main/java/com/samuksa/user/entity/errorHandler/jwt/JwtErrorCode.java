package com.samuksa.user.entity.errorHandler.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
public enum JwtErrorCode {
    ID_NOT_FOUND(400,"ID NOT FOUND"),
    ID_REGISTERED(400, "ID REGISTERED"),
    PW_NOT_MATCH(400,"PASSWORD NOT MATCH"),
    TOKEN_TIME_OUT(403, "TOKEN IS TIME OUT"),
    NON_TOKEN(401,"NON TOKEN"),
    INVALID_TOKEN(401,"INVALID_TOKEN"),
    UNKNOWN_ERROR(500, "UNKOWN_ERROR")
    ;


    private int code;
    private String message;
}
