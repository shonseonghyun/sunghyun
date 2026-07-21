package com.sunghyun.chat.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ChatParticipant {
    private Long ChatParticipantNo;

    private Long memberNo;

    private boolean isLeft;

    public static ChatParticipant createChatParticipant(Long memberNo){
        return ChatParticipant.builder()
                .memberNo(memberNo)
                .isLeft(false)
                .build()
                ;
    }
}
