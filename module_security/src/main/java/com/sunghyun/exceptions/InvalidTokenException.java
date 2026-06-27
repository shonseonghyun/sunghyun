package com.sunghyun.exceptions;

import com.sunghyun.web.ErrorCode;
import com.sunghyun.web.exception.BaseException;

public class InvalidTokenException extends BaseException {
    public InvalidTokenException(ErrorCode errorCode) {
        super(errorCode);
    }
}
