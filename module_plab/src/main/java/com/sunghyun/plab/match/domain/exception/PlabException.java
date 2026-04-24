package com.sunghyun.plab.match.domain.exception;

import com.sunghyun.web.exception.BaseException;
import com.sunghyun.web.ErrorCode;

public class PlabException extends BaseException {
    public PlabException(ErrorCode errorCode) {
        super(errorCode);
    }
}
