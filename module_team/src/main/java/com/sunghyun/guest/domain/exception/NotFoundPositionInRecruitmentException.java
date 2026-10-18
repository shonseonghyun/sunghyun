package com.sunghyun.guest.domain.exception;

import com.sunghyun.web.ErrorCode;

public class NotFoundPositionInRecruitmentException extends GuestRecruitmentException{
    public NotFoundPositionInRecruitmentException(ErrorCode errorCode) {
        super(errorCode);
    }
}
