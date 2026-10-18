package com.sunghyun.guest.domain.exception;

import com.sunghyun.web.ErrorCode;

public class NotFoundGuestRecruitmentException extends GuestRecruitmentException {
    public NotFoundGuestRecruitmentException(ErrorCode errorCode) {
        super(errorCode);
    }
}
