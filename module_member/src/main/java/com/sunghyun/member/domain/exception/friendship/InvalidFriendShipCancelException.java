package com.sunghyun.member.domain.exception.friendship;

import com.sunghyun.web.ErrorCode;
import com.sunghyun.web.exception.BaseException;

public class InvalidFriendShipCancelException extends BaseException {
    public InvalidFriendShipCancelException(ErrorCode errorCode) {
        super(errorCode);
    }
}
