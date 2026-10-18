package com.sunghyun.schedule.domain.exception;

import com.sunghyun.web.ErrorCode;

public class NoJoinedTeamException extends SchedulePollException{
    public NoJoinedTeamException(ErrorCode errorCode) {
        super(errorCode);
    }
}
