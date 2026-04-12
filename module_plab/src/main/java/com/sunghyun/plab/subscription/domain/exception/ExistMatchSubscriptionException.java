package com.sunghyun.plab.subscription.domain.exception;

import com.sunghyun.web.ErrorCode;

public class ExistMatchSubscriptionException extends SubscriptionException {
    public ExistMatchSubscriptionException(ErrorCode errorCode) {
        super(errorCode);
    }
}
