package com.sunghyun.chat.application.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sunghyun.chat.application.dto.enums.ChatEventType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WebSocketEventDto<T> {
    private ChatEventType eventType;
    private T data;

    public static <T> WebSocketEventDto of(ChatEventType eventType, T data) {
        return WebSocketEventDto.<T>builder()
                .eventType(eventType)
                .data(data)
                .build()
                ;
    }
}