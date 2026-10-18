package com.sunghyun.team.domain.exceptions.request;

import com.sunghyun.team.domain.exceptions.TeamJoinRequestException;
import com.sunghyun.web.ErrorCode;

public class TeamJoinAlreadyJoinedException extends TeamJoinRequestException {
    public TeamJoinAlreadyJoinedException(ErrorCode errorCode) {
        super(errorCode);
    }
}
