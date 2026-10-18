package com.sunghyun.guest.domain.exception;

import com.sunghyun.web.ErrorCode;

public class AlreadyGuestRecruitmentExistException extends GuestRecruitmentException{
    public AlreadyGuestRecruitmentExistException(ErrorCode errorCode) {
        super(errorCode);
    }
}
