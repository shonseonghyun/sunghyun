package com.sunghyun.chat.application.dto.res;

import com.sunghyun.chat.domain.message.ChatMessage;
import com.sunghyun.chat.domain.message.enums.ChatMessageType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChatMessageResDto {
    private Long chatMessageNo;
    private Long senderMemberNo;
    private String content;
    private ChatMessageType messageType;
    private String sendDt;
    private String sendTm;

    public static ChatMessageResDto fromDomain(ChatMessage chatMessage) {
        return ChatMessageResDto.builder()
                .chatMessageNo(chatMessage.getChatMessageNo())
                .senderMemberNo(chatMessage.getSenderMemberNo())
                .content(chatMessage.getContent())
                .messageType(chatMessage.getMessageType())
                .sendDt(chatMessage.getSendDt())
                .sendTm(chatMessage.getSendTm())
                .build();
    }
}