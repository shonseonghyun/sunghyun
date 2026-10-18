package com.sunghyun.team.domain.exceptions.request;

import com.sunghyun.team.domain.exceptions.TeamJoinRequestException;
import com.sunghyun.web.ErrorCode;

public class TeamAlreadyJoinedException extends TeamJoinRequestException {
    public TeamAlreadyJoinedException(ErrorCode errorCode) {
        super(errorCode);
    }
}
