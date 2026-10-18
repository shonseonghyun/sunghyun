package com.sunghyun.schedule.domain.exception;

import com.sunghyun.web.ErrorCode;

public class NotFoundSchedulePollException extends SchedulePollException{
    public NotFoundSchedulePollException(ErrorCode errorCode) {
        super(errorCode);
    }
}
