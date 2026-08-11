package com.sunghyun.chat.application.dto.res;

import com.sunghyun.chat.domain.room.ChatParticipant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ChatParticipantLastReadResDto {
    private Long memberNo;
    private Long lastReadChatMessageNo;

    public static ChatParticipantLastReadResDto fromDomain(ChatParticipant participant) {
        return ChatParticipantLastReadResDto.builder()
                .memberNo(participant.getMemberNo())
                .lastReadChatMessageNo(participant.getLastReadChatMessageNo())
                .build();
    }
}