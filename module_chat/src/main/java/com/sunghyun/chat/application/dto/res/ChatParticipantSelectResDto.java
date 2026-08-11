package com.sunghyun.chat.application.dto.res;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sunghyun.chat.domain.room.ChatParticipant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ChatParticipantSelectResDto {
    private Long chatParticipantNo;
    private Long memberNo;
    private Long lastReadChatMessageNo;

    @JsonProperty("isLeft")
    private boolean isLeft;

    // 💡 도메인 -> DTO 변환 메서드 추가
    public static ChatParticipantSelectResDto fromDomain(ChatParticipant participant) {
        if (participant == null) return null;

        return ChatParticipantSelectResDto.builder()
                .chatParticipantNo(participant.getChatParticipantNo())
                .memberNo(participant.getMemberNo())
                .lastReadChatMessageNo(participant.getLastReadChatMessageNo())
                .isLeft(participant.isLeft())
                .build();
    }
}