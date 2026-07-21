package com.sunghyun.chat.application.dto;

import com.sunghyun.chat.domain.ChatMessage;
import com.sunghyun.chat.domain.enums.ChatMessageType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ChatMessageSendResDto {
    private Long chatMessageNo;
    private Long chatRoomNo;
    private Long senderMemberNo;
    private String senderMemberName;
    private ChatMessageType messageType;
    private String content;
    private String sendDt;
    private String sendTm;

    // 💡 도메인 데이터와 실시간 전송용 name을 함께 조합하여 변환합니다.
    public static ChatMessageSendResDto fromDomain(ChatMessage domain, String senderMemberName) {
        if (domain == null) return null;

        return ChatMessageSendResDto.builder()
                .chatMessageNo(domain.getChatMessageNo())
                .chatRoomNo(domain.getChatRoomNo())
                .senderMemberNo(domain.getSenderMemberNo())
                .senderMemberName(senderMemberName) // 💡 여기에 이름 매핑!
                .messageType(domain.getMessageType())
                .content(domain.getContent())
                .sendDt(domain.getSendDt())
                .sendTm(domain.getSendTm())
                .build();
    }
}