package com.sunghyun.plab.match.domain.exception;

import com.sunghyun.web.ErrorCode;

public class InvalidPlabMatchException extends PlabException {
    public InvalidPlabMatchException(ErrorCode errorCode) {
        super(errorCode);
    }
}
