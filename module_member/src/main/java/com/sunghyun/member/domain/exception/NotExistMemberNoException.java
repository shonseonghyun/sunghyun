package com.sunghyun.member.domain.exception;

import com.sunghyun.web.ErrorCode;

public class NotExistMemberNoException extends MemberException{
    public NotExistMemberNoException(ErrorCode errorCode) {
        super(errorCode);
    }
}
