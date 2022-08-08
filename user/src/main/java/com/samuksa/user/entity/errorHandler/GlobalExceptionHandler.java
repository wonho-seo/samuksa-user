package com.samuksa.user.entity.errorHandler;

import com.samuksa.user.dto.error.ErrorResponse;
import com.samuksa.user.entity.errorHandler.db.DbException;
import com.samuksa.user.entity.errorHandler.jwt.CustomJwtException;
import com.samuksa.user.entity.errorHandler.jwt.JwtErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomJwtException.class)
    public ResponseEntity<ErrorResponse> handleCustomJwtException(CustomJwtException ex){
        log.error("jwtErrorException",ex);
        ErrorResponse response = new ErrorResponse(ex.getJwtErrorCode());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(DbException.class)
    public ResponseEntity<ErrorResponse> handleDbException(DbException ex){
        log.error("DBException",ex);
        ErrorResponse response = new ErrorResponse(ex.getDbErrorCode());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex){
        log.error("handleException",ex);
        ErrorResponse response = new ErrorResponse(JwtErrorCode.INTER_SERVER_ERROR);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
