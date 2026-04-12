package com.sunghyun.exception;

import com.sunghyun.web.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BaseException extends RuntimeException{
    private ErrorCode errorCode;
}
