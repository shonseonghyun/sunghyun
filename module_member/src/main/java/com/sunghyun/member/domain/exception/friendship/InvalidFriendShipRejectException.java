package com.sunghyun.member.domain.exception.friendship;

import com.sunghyun.web.ErrorCode;
import com.sunghyun.web.exception.BaseException;

public class InvalidFriendShipRejectException extends BaseException {
    public InvalidFriendShipRejectException(ErrorCode errorCode) {
        super(errorCode);
    }
}
