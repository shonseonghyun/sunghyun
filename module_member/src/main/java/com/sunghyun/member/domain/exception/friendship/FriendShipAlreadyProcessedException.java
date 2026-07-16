package com.sunghyun.member.domain.exception.friendship;

import com.sunghyun.web.ErrorCode;
import com.sunghyun.web.exception.BaseException;

public class FriendShipAlreadyProcessedException extends BaseException {
    public FriendShipAlreadyProcessedException(ErrorCode errorCode) {
        super(errorCode);
    }
}
