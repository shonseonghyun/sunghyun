package com.sunghyun.web.exception;

import com.sunghyun.web.ErrorCode;

public class ExternalResourceNotFoundException extends BaseException{
    public ExternalResourceNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
