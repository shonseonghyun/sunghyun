package com.sunghyun.chat.application.dto;

import com.sunghyun.chat.domain.enums.ChatMessageType;
import lombok.Getter;

@Getter
public class ChatMessageSendReqDto {
    private Long chatMessageNo;
    private Long senderMemberNo;
    private String name;
    private ChatMessageType messageType;
    private String content;
}
