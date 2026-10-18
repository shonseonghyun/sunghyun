package com.sunghyun.schedule.domain.exception;

import com.sunghyun.web.ErrorCode;
import com.sunghyun.web.exception.BaseException;

public class NotFoundLocationException extends BaseException {
    public NotFoundLocationException(ErrorCode errorCode) {
        super(errorCode);
    }
}
