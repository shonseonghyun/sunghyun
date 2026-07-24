package com.sunghyun.chat.application.dto.res;

import com.sunghyun.chat.domain.message.ChatMessage;
import com.sunghyun.chat.domain.message.enums.ChatMessageType;
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
    private Long receiverMemberNo;
    private ChatMessageType messageType;
    private String content;
    private String sendDt;
    private String sendTm;

    // 💡 도메인 데이터와 실시간 전송용 name을 함께 조합하여 변환합니다.
    public static ChatMessageSendResDto fromDomain(ChatMessage domain, String senderMemberName,Long receiverMemberNo) {
        if (domain == null) return null;

        return ChatMessageSendResDto.builder()
                .chatMessageNo(domain.getChatMessageNo())
                .chatRoomNo(domain.getChatRoomNo())
                .senderMemberNo(domain.getSenderMemberNo())
                .senderMemberName(senderMemberName)
                .receiverMemberNo(receiverMemberNo)
                .messageType(domain.getMessageType())
                .content(domain.getContent())
                .sendDt(domain.getSendDt())
                .sendTm(domain.getSendTm())
                .build();
    }
}