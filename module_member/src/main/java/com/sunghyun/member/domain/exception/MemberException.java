package com.sunghyun.member.domain.exception;


import com.sunghyun.web.exception.BaseException;
import com.sunghyun.web.ErrorCode;
import lombok.Getter;

@Getter
public class MemberException extends BaseException {

    public MemberException(ErrorCode errorCode) {
        super(errorCode);
    }
}
