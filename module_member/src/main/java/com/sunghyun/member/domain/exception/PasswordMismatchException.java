package com.sunghyun.member.domain.exception;

import com.sunghyun.web.ErrorCode;

public class PasswordMismatchException extends MemberException{
    public PasswordMismatchException(ErrorCode errorCode) {
        super(errorCode);
    }
}
