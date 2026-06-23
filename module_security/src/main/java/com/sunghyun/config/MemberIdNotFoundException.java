package com.sunghyun.config;

import com.sunghyun.web.ErrorCode;

public class MemberIdNotFoundException extends AuthenticationException{
    public MemberIdNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
