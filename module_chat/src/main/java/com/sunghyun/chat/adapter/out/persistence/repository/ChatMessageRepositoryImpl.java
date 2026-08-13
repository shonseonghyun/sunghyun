package com.sunghyun.chat.adapter.out.persistence.repository;

import com.sunghyun.chat.adapter.out.persistence.entity.ChatMessageEntity;
import com.sunghyun.chat.domain.message.ChatMessage;
import com.sunghyun.chat.domain.message.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ChatMessageRepositoryImpl implements ChatMessageRepository {
    private final SpringJpaChatMessageRepository springJpaChatMessageRepository;

    @Override
    public ChatMessage save(ChatMessage chatMessage) {
        return springJpaChatMessageRepository.save(ChatMessageEntity.fromDomain(chatMessage))
                .toDomain();
    }

    @Override
    public List<ChatMessage> findLatestMessagesByRoomNos(List<Long> roomNos) {
        return springJpaChatMessageRepository.findLatestMessagesByRoomNos(roomNos)
                .stream()
                .map(ChatMessageEntity::toDomain)
                .toList();
    }

    @Override
    public List<ChatMessage> findMessagesByRoomNo(Long chatRoomNo,Long firstViewableChatMessageNo, Long lastMessageNo, Pageable pageable) {
        return springJpaChatMessageRepository.findByChatRoomNoAndFirstViewable(chatRoomNo,firstViewableChatMessageNo,lastMessageNo,pageable)
                .stream().map(ChatMessageEntity::toDomain)
                .toList()
                ;
    }

    @Override
    public boolean existsByChatRoomNo(Long chatRoomNo) {
        return springJpaChatMessageRepository.existsByChatRoomNo(chatRoomNo);
    }
}