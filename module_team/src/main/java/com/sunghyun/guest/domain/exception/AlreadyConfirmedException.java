package com.sunghyun.guest.domain.exception;

import com.sunghyun.web.ErrorCode;

public class AlreadyConfirmedException extends GuestRecruitmentException{
    public AlreadyConfirmedException(ErrorCode errorCode) {
        super(errorCode);
    }
}
