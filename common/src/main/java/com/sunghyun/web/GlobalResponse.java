package com.sunghyun.web;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GlobalResponse<T> {
    private String code;
    private String message;
    private List<DetailMessage> detailMessages;
    private T data;

    // 1. 단순 에러 코드만 필요한 경우
    public static <T> GlobalResponse<T> of(ErrorCode errorCode){
        if(errorCode==null) return GlobalResponse.of(ErrorCode.F00);

        return GlobalResponse.<T>builder()
                .code(errorCode.name())
                .message(errorCode.getMessage())
                .build()
                ;
    }

    // 2. 상세 에러 메시지가 포함되는 경우 (질문하신 케이스)
    public static <T> GlobalResponse<T> of(ErrorCode errorCode,List<DetailMessage> detailMessages){
        return GlobalResponse.<T>builder()
                .code(errorCode.name())
                .message(errorCode.getMessage())
                .detailMessages(detailMessages)
                .build()
                ;
    }

    // 3. 데이터를 담는 경우
    public static <T> GlobalResponse<T> of(ErrorCode errorCode,T data){
        return GlobalResponse.<T>builder()
                .code(errorCode.name())
                .message(errorCode.getMessage())
                .data(data)
                .build()
                ;
    }
}