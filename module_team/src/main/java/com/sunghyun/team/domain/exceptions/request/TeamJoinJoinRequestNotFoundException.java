package com.sunghyun.team.domain.exceptions.request;

import com.sunghyun.team.domain.exceptions.TeamJoinRequestException;
import com.sunghyun.web.ErrorCode;

public class TeamJoinJoinRequestNotFoundException extends TeamJoinRequestException {
    public TeamJoinJoinRequestNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
