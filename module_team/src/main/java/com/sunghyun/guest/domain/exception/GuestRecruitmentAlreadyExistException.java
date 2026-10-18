package com.sunghyun.guest.domain.exception;

import com.sunghyun.web.ErrorCode;

public class GuestRecruitmentAlreadyExistException extends GuestRecruitmentException{
    public GuestRecruitmentAlreadyExistException(ErrorCode errorCode) {
        super(errorCode);
    }
}
