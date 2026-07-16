package com.sunghyun.member.domain.exception.friendship;

import com.sunghyun.web.ErrorCode;
import com.sunghyun.web.exception.BaseException;

public class NotFriendShipStatusException extends BaseException {
    public NotFriendShipStatusException(ErrorCode errorCode) {
        super(errorCode);
    }
}
