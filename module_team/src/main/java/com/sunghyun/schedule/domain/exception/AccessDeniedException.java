package com.sunghyun.schedule.domain.exception;

import com.sunghyun.web.ErrorCode;

public class AccessDeniedException extends SchedulePollException{
    public AccessDeniedException(ErrorCode errorCode) {
        super(errorCode);
    }
}
