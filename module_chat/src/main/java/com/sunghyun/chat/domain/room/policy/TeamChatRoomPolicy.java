package com.sunghyun.chat.domain.room.policy;

import com.sunghyun.chat.domain.room.ChatParticipant;

import java.util.List;

public class TeamChatRoomPolicy implements ChatRoomPolicy{
    @Override
    public List<Long> getReceiverMemberNos(List<ChatParticipant> participants, Long senderMemberNo) {
        return List.of();
    }

    @Override
    public void handleNewMessage(List<ChatParticipant> participants, Long chatMessageNo) {

    }
}
