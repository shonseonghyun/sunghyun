package com.sunghyun.guest.domain.exception;

import com.sunghyun.web.ErrorCode;

public class SelfApplicationNotAllowedException extends GuestRecruitmentException{
    public SelfApplicationNotAllowedException(ErrorCode errorCode) {
        super(errorCode);
    }
}
