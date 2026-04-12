package com.sunghyun.exception;

import com.sunghyun.web.ErrorCode;

public class MergeException extends BaseException{
    public MergeException(ErrorCode errorCode) {
        super(errorCode);
    }
}
