package com.sunghyun.member.domain.exception.friendship;

import com.sunghyun.web.ErrorCode;
import com.sunghyun.web.exception.BaseException;

public class FriendShipAlreadyDisconnectedException extends BaseException {
    public FriendShipAlreadyDisconnectedException(ErrorCode errorCode) {
        super(errorCode);
    }
}
