package com.sunghyun.chat.domain.room.repository;

import com.sunghyun.chat.domain.dto.UnreadCountMapping;
import com.sunghyun.chat.domain.room.ChatRoom;
import com.sunghyun.chat.domain.room.enums.ChatRoomType;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository {
    ChatRoom save(ChatRoom chatRoom);
    Optional<ChatRoom> findChatRoomByChatRoomNo(Long chatRoomNo);
    Optional<ChatRoom> findChatRoomByMemberNosAndChatRoomType(Long memberNo, Long friendMemberNo, ChatRoomType chatRoomType);
    List<UnreadCountMapping> findUnreadCountsByMemberNoAndRoomNos(Long memberNo, List<Long> roomNoList);
    List<ChatRoom> findChatRoomsByMemberNo(Long memberNo);
}
