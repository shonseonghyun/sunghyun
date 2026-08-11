package com.sunghyun.chat.domain.message.repository;

import com.sunghyun.chat.domain.message.ChatMessage;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ChatMessageRepository {
    ChatMessage save(ChatMessage chatMessage);
    List<ChatMessage> findLatestMessagesByRoomNos(List<Long> roomNoList);
    List<ChatMessage> findMessagesByRoomNo(Long chatRoomNo, Long lastMessageNo, Pageable pageable);
    boolean existsByChatRoomNo(Long chatRoomNo);
}
