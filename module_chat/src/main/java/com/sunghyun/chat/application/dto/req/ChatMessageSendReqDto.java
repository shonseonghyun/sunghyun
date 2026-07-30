package com.sunghyun.chat.application.dto.req;

import com.sunghyun.chat.domain.message.enums.ChatMessageType;
import lombok.Getter;

@Getter
public class ChatMessageSendReqDto {
    private String senderName;
    private ChatMessageType messageType;
    private String content;
}
