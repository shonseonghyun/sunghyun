package com.sunghyun.chat.application.dto.res;

import com.sunghyun.chat.domain.message.ChatMessage;
import com.sunghyun.chat.domain.message.enums.ChatMessageType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class ChatMessageResDto {
    private Long chatMessageNo;
//    private Long chatRoomNo;
    private Long senderMemberNo;
    private String senderMemberName;
    private List<Long> receiverMembersNo;
    private ChatMessageType messageType;
    private String content;
    private String sendDt;
    private String sendTm;

    // 💡 도메인 데이터와 실시간 전송용 name을 함께 조합하여 변환합니다.
    public static ChatMessageResDto fromDomain(ChatMessage domain, List<Long> receiverMembersNo) {
        if (domain == null) return null;

        return ChatMessageResDto.builder()
                .chatMessageNo(domain.getChatMessageNo())
//                .chatRoomNo(domain.getChatRoomNo())
                .senderMemberNo(domain.getSenderMemberNo())
                .senderMemberName(String.valueOf(domain.getSenderMemberNo()))
                .receiverMembersNo(receiverMembersNo)
                .messageType(domain.getMessageType())
                .content(domain.getContent())
                .sendDt(domain.getSendDt())
                .sendTm(domain.getSendTm())
                .build();
    }
}