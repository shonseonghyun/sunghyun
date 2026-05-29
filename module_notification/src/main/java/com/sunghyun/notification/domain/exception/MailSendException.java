package com.sunghyun.notification.domain.exception;

import com.sunghyun.web.ErrorCode;
import com.sunghyun.web.exception.BaseException;

public class MailSendException extends BaseException {
    public MailSendException(ErrorCode errorCode) {
        super(errorCode);
    }
}
