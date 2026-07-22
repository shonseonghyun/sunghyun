package com.sunghyun.chat.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ChatReadResDto {
    private Long chatRoomNo;
    private Long memberNo;
    private Long lastReadChatMessageNo;
}