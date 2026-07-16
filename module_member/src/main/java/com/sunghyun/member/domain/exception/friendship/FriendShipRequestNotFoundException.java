package com.sunghyun.member.domain.exception.friendship;

import com.sunghyun.web.ErrorCode;
import com.sunghyun.web.exception.BaseException;

public class FriendShipRequestNotFoundException extends BaseException {
    public FriendShipRequestNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
