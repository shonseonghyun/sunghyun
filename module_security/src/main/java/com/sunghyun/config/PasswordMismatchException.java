package com.sunghyun.config;

import com.sunghyun.web.ErrorCode;

public class PasswordMismatchException extends AuthenticationException{
    public PasswordMismatchException(ErrorCode errorCode) {
        super(errorCode);
    }
}
