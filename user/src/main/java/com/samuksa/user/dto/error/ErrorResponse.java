package com.samuksa.user.dto.error;

import com.samuksa.user.entity.errorHandler.db.DbErrorCode;
import com.samuksa.user.entity.errorHandler.jwt.JwtErrorCode;
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
}
