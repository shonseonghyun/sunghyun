package com.sunghyun.config;

import com.sunghyun.web.ErrorCode;
import com.sunghyun.web.exception.BaseException;

public abstract class AuthenticationException extends BaseException {
    public AuthenticationException(ErrorCode errorCode) {
        super(errorCode);
    }
}
