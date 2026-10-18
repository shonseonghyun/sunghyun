package com.sunghyun.schedule.domain.exception;

import com.sunghyun.web.ErrorCode;
import com.sunghyun.web.exception.BaseException;

public class SchedulePollException extends BaseException {
    public SchedulePollException(ErrorCode errorCode) {
        super(errorCode);
    }
}
