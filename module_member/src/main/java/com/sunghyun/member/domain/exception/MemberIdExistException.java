package com.sunghyun.member.domain.exception;

import com.sunghyun.web.ErrorCode;

public class MemberIdExistException extends MemberException{
    public MemberIdExistException(ErrorCode errorCode) {
        super(errorCode);
    }
}
