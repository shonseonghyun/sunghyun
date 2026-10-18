package com.sunghyun.team.domain.exceptions.team;

import com.sunghyun.team.domain.exceptions.TeamException;
import com.sunghyun.web.ErrorCode;

public class TeamNotFoundException extends TeamException {
    public TeamNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
