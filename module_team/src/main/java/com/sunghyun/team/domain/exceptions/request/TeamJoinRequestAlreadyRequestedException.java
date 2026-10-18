package com.sunghyun.team.domain.exceptions.request;

import com.sunghyun.team.domain.exceptions.TeamJoinRequestException;
import com.sunghyun.web.ErrorCode;

public class TeamJoinRequestAlreadyRequestedException extends TeamJoinRequestException {
    public TeamJoinRequestAlreadyRequestedException(ErrorCode errorCode) {
        super(errorCode);
    }
}