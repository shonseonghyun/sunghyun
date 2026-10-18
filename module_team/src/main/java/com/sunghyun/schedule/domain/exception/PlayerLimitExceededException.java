package com.sunghyun.schedule.domain.exception;

import com.sunghyun.web.ErrorCode;

public class PlayerLimitExceededException extends SchedulePollException{
    public PlayerLimitExceededException(ErrorCode errorCode) {
        super(errorCode);
    }
}
