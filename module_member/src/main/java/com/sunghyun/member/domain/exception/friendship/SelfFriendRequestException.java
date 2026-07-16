package com.sunghyun.member.domain.exception.friendship;

import com.sunghyun.web.ErrorCode;
import com.sunghyun.web.exception.BaseException;

public class SelfFriendRequestException extends BaseException {
    public SelfFriendRequestException(ErrorCode errorCode) {
        super(errorCode);
    }
}
