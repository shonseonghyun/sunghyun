package com.sunghyun.member.domain.exception.friendship;

import com.sunghyun.web.ErrorCode;
import com.sunghyun.web.exception.BaseException;

public class FriendShipAlreadyRequestedException extends BaseException {
    public FriendShipAlreadyRequestedException(ErrorCode errorCode) {
        super(errorCode);
    }
}
