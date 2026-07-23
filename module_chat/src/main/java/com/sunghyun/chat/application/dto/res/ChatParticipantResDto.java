package com.sunghyun.chat.application.dto.res;

import com.sunghyun.chat.domain.ChatParticipant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ChatParticipantResDto {
    private Long memberNo;
    private Long lastReadChatMessageNo;

    public static ChatParticipantResDto fromDomain(ChatParticipant participant) {
        return ChatParticipantResDto.builder()
                .memberNo(participant.getMemberNo())
                .lastReadChatMessageNo(participant.getLastReadChatMessageNo())
                .build();
    }
}