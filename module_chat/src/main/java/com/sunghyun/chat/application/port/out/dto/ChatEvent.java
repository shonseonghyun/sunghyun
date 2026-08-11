package com.sunghyun.chat.application.port.out.dto;

import com.sunghyun.chat.application.dto.res.ChatMessageResDto;
import com.sunghyun.chat.application.dto.res.ChatReadResDto;
import lombok.Getter;

@Getter
public class ChatEvent {
    public record MessageCreated(
            Long chatRoomNo,
            ChatMessageResDto result
    ) {}

    public record MessageRead(
            Long chatRoomNo,
            ChatReadResDto result
    ) {}

    public record ChatRoomLeaved(
            Long chatRoomNo,
            ChatMessageResDto result
    ) {}

    public record ChatRoomInvited(
            Long chatRoomNo,
            ChatMessageResDto result
    ) {}
}
