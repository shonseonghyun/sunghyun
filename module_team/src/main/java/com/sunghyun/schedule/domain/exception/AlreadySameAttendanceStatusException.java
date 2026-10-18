package com.sunghyun.schedule.domain.exception;

import com.sunghyun.web.ErrorCode;

public class AlreadySameAttendanceStatusException extends SchedulePollException{
    public AlreadySameAttendanceStatusException(ErrorCode errorCode) {
        super(errorCode);
    }
}
