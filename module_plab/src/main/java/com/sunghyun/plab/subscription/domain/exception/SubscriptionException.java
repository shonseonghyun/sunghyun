package com.sunghyun.plab.subscription.domain.exception;

import com.sunghyun.exception.BaseException;
import com.sunghyun.web.ErrorCode;

public class SubscriptionException extends BaseException {
    public SubscriptionException(ErrorCode errorCode) {
        super(errorCode);
    }
}
