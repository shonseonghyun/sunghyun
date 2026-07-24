package com.sunghyun.chat.domain.exception;

import com.sunghyun.web.ErrorCode;
import com.sunghyun.web.exception.BaseException;

public class NotFoundChatParticipantException extends BaseException {
    public NotFoundChatParticipantException(ErrorCode errorCode) {
        super(errorCode);
    }
}
