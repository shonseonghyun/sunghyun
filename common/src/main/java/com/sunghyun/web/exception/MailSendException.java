package com.sunghyun.web.exception;

import com.sunghyun.web.ErrorCode;

public class MailSendException extends BaseException{
    public MailSendException(ErrorCode errorCode) {
        super(errorCode);
    }
}
