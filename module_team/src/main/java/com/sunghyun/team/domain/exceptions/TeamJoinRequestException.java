package com.sunghyun.team.domain.exceptions;

import com.sunghyun.web.ErrorCode;
import com.sunghyun.web.exception.BaseException;

public class TeamJoinRequestException extends BaseException {
    public TeamJoinRequestException(ErrorCode errorCode) {
        super(errorCode);
    }
}
