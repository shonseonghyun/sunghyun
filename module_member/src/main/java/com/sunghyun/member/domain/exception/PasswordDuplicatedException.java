package com.sunghyun.member.domain.exception;

import com.sunghyun.web.ErrorCode;
import com.sunghyun.web.exception.BaseException;

public class PasswordDuplicatedException extends BaseException {
    public PasswordDuplicatedException(ErrorCode errorCode) {
        super(errorCode);
    }
}
