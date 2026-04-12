package com.sunghyun.member.domain.exception;

import com.sunghyun.web.ErrorCode;

public class AlreadyExistMemberIdException extends MemberException{
    public AlreadyExistMemberIdException(ErrorCode errorCode) {
        super(errorCode);
    }
}
