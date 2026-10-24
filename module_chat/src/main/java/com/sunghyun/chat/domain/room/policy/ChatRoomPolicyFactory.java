package com.sunghyun.chat.domain.room.policy;

import com.sunghyun.chat.domain.room.enums.ChatRoomType;

import java.util.Map;

public class ChatRoomPolicyFactory {

    // 스프링 IOC 기능을 쓰지 않은 이유는 도메인 계층은 최대한 POJO를 유지해야 하므로
    private static final Map<ChatRoomType, ChatRoomPolicy> POLICIES = Map.of(
            ChatRoomType.PRIVATE, new PrivateChatRoomPolicy(),
            ChatRoomType.GROUP, new GroupChatRoomPolicy(),
            ChatRoomType.TEAM, new TeamChatRoomPolicy()
    );

    public static ChatRoomPolicy getPolicy(ChatRoomType chatRoomType) {
        ChatRoomPolicy policy = POLICIES.get(chatRoomType);
        if (policy == null) {
            throw new IllegalArgumentException("지원하지 않는 채팅방 타입입니다: " + chatRoomType);
        }
        return policy;
    }

}
