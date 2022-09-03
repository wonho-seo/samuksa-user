package com.samuksa.user.entity.errorHandler.jwt;

import io.jsonwebtoken.JwtException;
import lombok.Getter;

@Getter
public class CustomJwtException extends RuntimeException{

    private JwtErrorCode jwtErrorCode;

    public CustomJwtException(String message, JwtErrorCode jwtErrorCode){
        super(message);
        this.jwtErrorCode = jwtErrorCode;
    }
}
