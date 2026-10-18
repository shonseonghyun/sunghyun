package com.sunghyun.guest.domain.exception;

import com.sunghyun.web.ErrorCode;

public class MatchConflictException extends GuestRecruitmentException{
    public MatchConflictException(ErrorCode errorCode) {
        super(errorCode);
    }
}
