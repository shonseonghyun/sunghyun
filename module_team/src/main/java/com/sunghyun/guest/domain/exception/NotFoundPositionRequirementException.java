package com.sunghyun.guest.domain.exception;

import com.sunghyun.web.ErrorCode;

public class NotFoundPositionRequirementException extends GuestRecruitmentException{
    public NotFoundPositionRequirementException(ErrorCode errorCode) {
        super(errorCode);
    }
}
