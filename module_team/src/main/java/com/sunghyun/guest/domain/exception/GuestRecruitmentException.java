package com.sunghyun.guest.domain.exception;

import com.sunghyun.web.ErrorCode;
import com.sunghyun.web.exception.BaseException;

public class GuestRecruitmentException extends BaseException {
    public GuestRecruitmentException(ErrorCode errorCode) {
        super(errorCode);
    }
}
