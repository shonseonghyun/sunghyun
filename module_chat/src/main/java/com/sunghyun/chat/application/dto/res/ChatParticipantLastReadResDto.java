package com.sunghyun.chat.application.dto.res;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty("isLeft")
    private boolean isLeft;

    public static ChatParticipantLastReadResDto fromDomain(ChatParticipant participant) {
        return ChatParticipantLastReadResDto.builder()
                .memberNo(participant.getMemberNo())
                .lastReadChatMessageNo(participant.getLastReadChatMessageNo())
                .isLeft(participant.isLeft())
                .build();
    }
}