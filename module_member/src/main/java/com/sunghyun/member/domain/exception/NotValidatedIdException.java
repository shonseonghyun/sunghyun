package com.sunghyun.member.domain.exception;

import com.sunghyun.web.ErrorCode;

public class NotValidatedIdException extends MemberException{
    public NotValidatedIdException(ErrorCode errorCode) {
        super(errorCode);
    }
}
