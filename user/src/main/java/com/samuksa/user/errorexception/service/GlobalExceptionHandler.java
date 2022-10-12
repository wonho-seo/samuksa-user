package com.samuksa.user.errorexception.service;

import com.samuksa.user.errorexception.dto.ErrorResponse;
import com.samuksa.user.errorexception.entity.errorHandler.db.DbException;
import com.samuksa.user.errorexception.entity.errorHandler.email.EmailException;
import com.samuksa.user.errorexception.entity.errorHandler.jwt.CustomJwtException;
import com.samuksa.user.errorexception.entity.errorHandler.jwt.JwtErrorCode;
import com.samuksa.user.errorexception.entity.errorHandler.signup.SignupException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomJwtException.class)
    public ResponseEntity<ErrorResponse> handleCustomJwtException(CustomJwtException ex){
        log.error("jwtErrorException",ex);
        ErrorResponse response = new ErrorResponse(ex.getJwtErrorCode());
        return ResponseEntity.status(ex.getJwtErrorCode().getCode()).body(response);
    }

    @ExceptionHandler(DbException.class)
    public ResponseEntity<ErrorResponse> handleDbException(DbException ex){
        log.error("DBException",ex);
        ErrorResponse response = new ErrorResponse(ex.getDbErrorCode());
        return ResponseEntity.status(ex.getDbErrorCode().getCode()).body(response);
    }
    @ExceptionHandler(EmailException.class)
    public ResponseEntity<ErrorResponse> handleEmailException(EmailException ex){
        log.error("EmialException",ex);
        ErrorResponse response = new ErrorResponse(ex.getEmailErrorCode());
        return ResponseEntity.status(ex.getEmailErrorCode().getCode()).body(response);
    }
    @ExceptionHandler(SignupException.class)
    public ResponseEntity<ErrorResponse> handleSignupException(SignupException ex){
        log.error("SignupException",ex);
        ErrorResponse response = new ErrorResponse(ex.getSignupErrorCode());
        return ResponseEntity.status(ex.getSignupErrorCode().getCode()).body(response);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex){
        log.error("handleException",ex);
        ErrorResponse response = new ErrorResponse(JwtErrorCode.UNKNOWN_ERROR);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex){
        ErrorResponse response = new ErrorResponse(ex);
        return ResponseEntity.status(response.getCode()).body(response);
    }
}
