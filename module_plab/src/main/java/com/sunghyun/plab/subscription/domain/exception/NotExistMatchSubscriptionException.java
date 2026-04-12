package com.sunghyun.plab.subscription.domain.exception;

import com.sunghyun.web.ErrorCode;

public class NotExistMatchSubscriptionException extends SubscriptionException {
    public NotExistMatchSubscriptionException(ErrorCode errorCode) {
        super(errorCode);
    }
}
