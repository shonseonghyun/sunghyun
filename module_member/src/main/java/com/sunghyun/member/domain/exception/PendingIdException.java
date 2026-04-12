package com.sunghyun.member.domain.exception;

import com.sunghyun.web.ErrorCode;

public class PendingIdException extends MemberException{
    public PendingIdException(ErrorCode errorCode) {
        super(errorCode);
    }
}
