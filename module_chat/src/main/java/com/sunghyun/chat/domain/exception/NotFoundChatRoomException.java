package com.sunghyun.chat.domain.exception;

import com.sunghyun.web.ErrorCode;

public class NotFoundChatRoomException extends ChatException {
    public NotFoundChatRoomException(ErrorCode errorCode) {
        super(errorCode);
    }
}
