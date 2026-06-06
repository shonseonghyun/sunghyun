package com.sunghyun.plab.subscription.domain.exception;

import com.sunghyun.web.ErrorCode;
import com.sunghyun.web.exception.BaseException;

public class NotExistMatchSubscriptionOutBoxException extends BaseException {
    public NotExistMatchSubscriptionOutBoxException(ErrorCode errorCode) {
        super(errorCode);
    }
}
