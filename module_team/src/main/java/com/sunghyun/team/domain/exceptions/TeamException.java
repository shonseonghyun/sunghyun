package com.sunghyun.team.domain.exceptions;

import com.sunghyun.web.ErrorCode;
import com.sunghyun.web.exception.BaseException;

public class TeamException extends BaseException {
    public TeamException(ErrorCode errorCode) {
        super(errorCode);
    }
}
