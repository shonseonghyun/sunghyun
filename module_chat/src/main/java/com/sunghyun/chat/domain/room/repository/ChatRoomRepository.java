package com.sunghyun.chat.domain.room.repository;

import com.sunghyun.chat.application.dto.res.UnreadCountMapping;
import com.sunghyun.chat.domain.room.ChatParticipant;
import com.sunghyun.chat.domain.room.ChatRoom;
import com.sunghyun.chat.domain.room.enums.ChatRoomType;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository {
    ChatRoom save(ChatRoom chatRoom);
    ChatParticipant save(ChatParticipant chatParticipant);
    Optional<ChatRoom> findChatRoomByChatRoomNo(Long chatRoomNo);
    Optional<ChatRoom> findChatRoomByMemberNosAndChatRoomType(Long memberNo, Long friendMemberNo, ChatRoomType chatRoomType);
    List<ChatRoom> findChatRoomsByMemberNoAndChatRoomType(Long memberNo, ChatRoomType chatRoomType);
    List<UnreadCountMapping> findUnreadCountsByMemberNoAndRoomNos(Long memberNo, List<Long> roomNoList);
}
