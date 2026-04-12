package com.sunghyun.member.domain.exception;

import com.sunghyun.web.ErrorCode;

public class NotExistUpdateMemberException extends MemberException {
    public NotExistUpdateMemberException(ErrorCode errorCode) {
        super(errorCode);
    }
}
