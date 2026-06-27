package com.sunghyun.config;

import com.sunghyun.web.ErrorCode;

public class MemberNotFoundException extends AuthenticationException{
    public MemberNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
