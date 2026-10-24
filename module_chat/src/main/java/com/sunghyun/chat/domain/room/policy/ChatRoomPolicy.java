package com.sunghyun.chat.domain.room.policy;

import com.sunghyun.chat.domain.room.ChatParticipant;

import java.util.List;

public interface ChatRoomPolicy {
    // 1. 메시지 수신 대상자 추출 정책
    List<Long> getReceiverMemberNos(List<ChatParticipant> participants, Long senderMemberNo);

    // 2. 새 메시지 수신 시 참여자 상태 업데이트 정책
    void handleNewMessage(List<ChatParticipant> participants, Long chatMessageNo);
}