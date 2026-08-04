package com.sunghyun.chat.domain.exception;

import com.sunghyun.web.ErrorCode;

public class InvalidParticipantCountException extends ChatException{
    public InvalidParticipantCountException(ErrorCode errorCode) {
        super(errorCode);
    }
}
