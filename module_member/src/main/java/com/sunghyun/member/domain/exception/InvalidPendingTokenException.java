package com.sunghyun.member.domain.exception;

import com.sunghyun.web.ErrorCode;

public class InvalidPendingTokenException extends MemberException{
    public InvalidPendingTokenException(ErrorCode errorCode) {
        super(errorCode);
    }
}
