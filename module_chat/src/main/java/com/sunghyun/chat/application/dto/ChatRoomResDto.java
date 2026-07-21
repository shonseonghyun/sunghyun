package com.sunghyun.chat.application.dto;

import com.sunghyun.chat.domain.enums.ChatMessageType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ChatRoomResDto {
    private Long chatRoomNo;
    private Long friendMemberNo;

    private ChatMessageType messageType;
    private Long lastSenderMemberNo;
    private String lastContent;

    private String lastSendDt;
    private String lastSendTm;

    private Long unreadCount; // 안 읽은 메시지 수 추가
}
