package com.sunghyun.chat.application.port.out.dto;

import com.sunghyun.chat.application.dto.res.ChatMessageSendResDto;
import com.sunghyun.chat.application.dto.res.ChatReadResDto;
import lombok.Getter;

@Getter
public class ChatEvent {
    public record MessageCreated(
            Long chatRoomNo,
            ChatMessageSendResDto result
    ) {}

    public record MessageRead(
            Long chatRoomNo,
            ChatReadResDto result
    ) {}
}
