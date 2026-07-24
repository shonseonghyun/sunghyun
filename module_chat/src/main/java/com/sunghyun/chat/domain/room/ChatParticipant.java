package com.sunghyun.chat.domain.room;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ChatParticipant {
    private Long chatParticipantNo;

    private Long memberNo;

    private Long lastReadChatMessageNo;

    private boolean isLeft;

    public static ChatParticipant createChatParticipant(Long memberNo){
        return ChatParticipant.builder()
                .memberNo(memberNo)
                .isLeft(false)
                .lastReadChatMessageNo(0L)
                .build();
    }

    public void readMessage(Long lastReadChatMessageNo) {
        if (lastReadChatMessageNo == null) return;

        if (this.lastReadChatMessageNo == null || lastReadChatMessageNo > this.lastReadChatMessageNo) {
            this.lastReadChatMessageNo = lastReadChatMessageNo;
        }
    }
}