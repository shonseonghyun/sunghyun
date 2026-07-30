package com.sunghyun.chat.domain.exception;

import com.sunghyun.web.ErrorCode;

public class NotFoundChatParticipantException extends ChatException {
    public NotFoundChatParticipantException(ErrorCode errorCode) {
        super(errorCode);
    }
}
