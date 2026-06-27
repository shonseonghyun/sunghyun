package com.sunghyun.web;

import com.sunghyun.web.exception.BaseException;
import feign.RetryableException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<GlobalResponse> handleBaseException(BaseException baseException){
        ErrorCode errorCode = baseException.getErrorCode();
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(GlobalResponse.of(errorCode))
                ;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public GlobalResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException exception){
        List<DetailMessage> detailMessages = new ArrayList<>();

        List<FieldError> filedErrors = exception.getBindingResult().getFieldErrors();
        for(FieldError fieldError:filedErrors){
            detailMessages.add(
                    new DetailMessage(
                            fieldError.getField(),
                            fieldError.getRejectedValue()==null ? null : fieldError.getRejectedValue().toString(),
                            fieldError.getDefaultMessage()
                    )
            );
        }

        return GlobalResponse.of(ErrorCode.F000,detailMessages);
    }

    /**
     * @PathVariable 타입이 다른 경우
     *
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public GlobalResponse handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException exception){
        List<DetailMessage> detailMessages = new ArrayList<>();

        String fieldName = exception.getPropertyName();
        String rejectedValue= exception.getValue()==null ? null : exception.getValue().toString();
        detailMessages.add(
                new DetailMessage(
                        fieldName,
                        rejectedValue,
                        "올바르지 않은 필드가 인입되었습니다."
                )
        );

        return GlobalResponse.of(ErrorCode.F000,detailMessages);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public GlobalResponse handleConstraintViolationException(ConstraintViolationException exception){
        List<DetailMessage> detailMessages = new ArrayList<>();

        for(ConstraintViolation<?> violation: exception.getConstraintViolations()){
            String fullPath = violation.getPropertyPath().toString();
            String fieldName = fullPath.substring(fullPath.lastIndexOf('.')+1);
            String message = violation.getMessage();
            String rejectedValue = violation.getInvalidValue() == null ? null : violation.getInvalidValue().toString();

            detailMessages.add(
                    new DetailMessage(
                            fieldName,
                            rejectedValue,
                            message
                    )
            );
        }

        return GlobalResponse.of(ErrorCode.F000,detailMessages);
    }

    /*
    * case1. 요청바디(RequestBody) 자체가 누락된 경우
    * case2. Enum 형식이 올바르지 않은 경우
    */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public GlobalResponse handleHttpMessageNotReadableException(HttpMessageNotReadableException e){
        log.error("JSON 파싱 에러 발생 (Http Body 구조적 오류): {}", e.getMessage());

        return GlobalResponse.of(ErrorCode.C400);
    }

    @ExceptionHandler({NoResourceFoundException.class,NoHandlerFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND) // 404
    public GlobalResponse handle404(Exception e) {
        return GlobalResponse.of(ErrorCode.C404);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED) // 405
    public GlobalResponse handle405(HttpRequestMethodNotSupportedException e) {
        return GlobalResponse.of(ErrorCode.C405);
    }

    @ExceptionHandler(RetryableException.class)
    @ResponseStatus(HttpStatus.GATEWAY_TIMEOUT)
    public GlobalResponse handleFeignException(final RetryableException e){
        final String url = e.request().url();
        final String httpMethod = e.request().httpMethod().toString();

        log.error("외부 API 호출 타임아웃 발생! 메소드 타입: [{}], URL: [{}]", httpMethod, url);

        return GlobalResponse.of(ErrorCode.C504);
    }
}
