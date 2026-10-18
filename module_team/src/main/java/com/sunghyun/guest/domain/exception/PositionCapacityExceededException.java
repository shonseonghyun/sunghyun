package com.sunghyun.guest.domain.exception;

import com.sunghyun.web.ErrorCode;

public class PositionCapacityExceededException extends GuestRecruitmentException{
    public PositionCapacityExceededException(ErrorCode errorCode) {
        super(errorCode);
    }
}
