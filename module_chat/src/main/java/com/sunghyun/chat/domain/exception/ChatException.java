package com.sunghyun.chat.domain.exception;

import com.sunghyun.web.ErrorCode;
import com.sunghyun.web.exception.BaseException;

public class ChatException extends BaseException {
    public ChatException(ErrorCode errorCode) {
        super(errorCode);
    }
}
