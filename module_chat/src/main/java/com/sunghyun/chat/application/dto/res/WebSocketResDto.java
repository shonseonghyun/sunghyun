package com.sunghyun.chat.application.dto.res;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sunghyun.chat.application.dto.enums.ChatEventType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WebSocketResDto<T> {
    private ChatEventType eventType;
    private T data;

    public static <T> WebSocketResDto of(ChatEventType eventType, T data) {
        return WebSocketResDto.<T>builder()
                .eventType(eventType)
                .data(data)
                .build()
                ;
    }
}