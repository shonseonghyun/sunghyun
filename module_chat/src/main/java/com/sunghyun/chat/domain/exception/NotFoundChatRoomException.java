package com.sunghyun.chat.domain.exception;

import com.sunghyun.web.ErrorCode;
import com.sunghyun.web.exception.BaseException;

public class NotFoundChatRoomException extends BaseException {
    public NotFoundChatRoomException(ErrorCode errorCode) {
        super(errorCode);
    }
}
