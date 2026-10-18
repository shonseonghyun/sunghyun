package com.sunghyun.guest.domain.exception;

import com.sunghyun.web.ErrorCode;

public class GuestRecruitmentClosedException extends GuestRecruitmentException{
    public GuestRecruitmentClosedException(ErrorCode errorCode) {
        super(errorCode);
    }
}
