package com.sunghyun.member.domain.exception.friendship;

import com.sunghyun.web.ErrorCode;
import com.sunghyun.web.exception.BaseException;

public class InvalidFriendShipAcceptException extends BaseException {
    public InvalidFriendShipAcceptException(ErrorCode errorCode) {
        super(errorCode);
    }
}
