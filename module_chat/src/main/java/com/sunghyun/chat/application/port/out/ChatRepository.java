package com.sunghyun.chat.application.port.out;

import com.sunghyun.chat.domain.ChatMessage;
import com.sunghyun.chat.domain.ChatParticipant;
import com.sunghyun.chat.domain.ChatRoom;
import com.sunghyun.chat.domain.enums.ChatRoomType;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ChatRepository {
    ChatRoom save(ChatRoom chatRoom);
    ChatMessage save(ChatMessage chatMessage);
    ChatParticipant save(ChatParticipant chatParticipant);
    Optional<ChatRoom> findChatRoomByChatRoomNo(Long chatRoomNo);
    Optional<ChatRoom> findChatRoomByMemberNosAndChatRoomType(Long memberNo, Long friendMemberNo, ChatRoomType chatRoomType);
    List<ChatRoom> findChatRoomsByMemberNoAndChatRoomType(Long memberNo, ChatRoomType chatRoomType);
    List<ChatMessage> findLatestMessagesByRoomNos(List<Long> roomNoList);
    List<ChatMessage> findMessagesByRoomNo(Long chatRoomNo, Long lastMessageNo, Pageable pageable);
    List<com.sunghyun.chat.application.dto.res.UnreadCountMapping> findUnreadCountsByMemberNoAndRoomNos(Long memberNo, List<Long> roomNoList);
}
