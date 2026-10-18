package com.sunghyun.schedule.domain.exception;

import com.sunghyun.web.ErrorCode;
import com.sunghyun.web.exception.BaseException;

public class SchedulePollClosedException extends BaseException {
    public SchedulePollClosedException(ErrorCode errorCode) {
        super(errorCode);
    }
}
