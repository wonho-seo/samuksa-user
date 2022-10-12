package com.samuksa.user.errorexception.entity.errorHandler.db;

import lombok.Getter;

@Getter
public class DbException extends RuntimeException{
    private DbErrorCode dbErrorCode;

    public DbException(String message, DbErrorCode dbErrorCode){
        super(message);
        this.dbErrorCode = dbErrorCode;
    }
}
