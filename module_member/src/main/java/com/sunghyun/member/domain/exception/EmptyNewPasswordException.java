package com.sunghyun.member.domain.exception;


import com.sunghyun.web.ErrorCode;

public class EmptyNewPasswordException extends MemberException{
    public EmptyNewPasswordException(ErrorCode errorCode) {
        super(errorCode);
    }
}
