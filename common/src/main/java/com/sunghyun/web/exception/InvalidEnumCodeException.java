package com.sunghyun.web.exception;

import com.sunghyun.web.ErrorCode;

public class InvalidEnumCodeException extends BaseException{
    public InvalidEnumCodeException(ErrorCode errorCode) {
        super(errorCode);
    }
}
