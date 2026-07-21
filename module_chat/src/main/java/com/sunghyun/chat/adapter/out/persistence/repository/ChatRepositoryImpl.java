package com.sunghyun.chat.adapter.out.persistence.repository;

import com.sunghyun.chat.adapter.out.persistence.entity.ChatMessageEntity;
import com.sunghyun.chat.adapter.out.persistence.entity.ChatParticipantEntity;
import com.sunghyun.chat.adapter.out.persistence.entity.ChatRoomEntity;
import com.sunghyun.chat.application.dto.UnreadCountMapping;
import com.sunghyun.chat.application.port.out.ChatRepository;
import com.sunghyun.chat.domain.ChatMessage;
import com.sunghyun.chat.domain.ChatParticipant;
import com.sunghyun.chat.domain.ChatRoom;
import com.sunghyun.chat.domain.enums.ChatRoomType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ChatRepositoryImpl implements ChatRepository {
    private final SpringJpaChatRoomRepository springJpaChatRoomRepository;
    private final SpringJpaChatMessageRepository springJpaChatMessageRepository;
    private final SpringJpaChatParticipantRepository springJpaChatParticipantRepository;

    @Override
    public ChatRoom save(ChatRoom chatRoom) {
        return springJpaChatRoomRepository.save(ChatRoomEntity.fromDomain(chatRoom))
                .toDomain();
    }

    @Override
    public ChatMessage save(ChatMessage chatMessage) {
        return springJpaChatMessageRepository.save(ChatMessageEntity.fromDomain(chatMessage))
                .toDomain();
    }

    @Override
    public ChatParticipant save(ChatParticipant chatParticipant) {
        return springJpaChatParticipantRepository.save(ChatParticipantEntity.fromDomain(chatParticipant))
                .toDomain();
    }

    @Override
    public Optional<ChatRoom> findChatRoomByChatRoomNo(Long chatRoomNo) {
        return springJpaChatRoomRepository.findById(chatRoomNo)
                .map(ChatRoomEntity::toDomain)
                ;
    }

    @Override
    public Optional<ChatRoom> findChatRoomByMemberNosAndChatRoomType(Long memberNo, Long friendMemberNo, ChatRoomType chatRoomType) {
        return springJpaChatRoomRepository.findChatRoomByMemberNosAndChatRoomType(memberNo,friendMemberNo,chatRoomType)
                .map(ChatRoomEntity::toDomain)
                ;
    }

    @Override
    public List<ChatRoom> findChatRoomsByMemberNoAndChatRoomType(Long memberNo, ChatRoomType chatRoomType) {
        return springJpaChatRoomRepository.findChatRoomsByMemberNoAndChatRoomType(memberNo, chatRoomType)
                .stream()
                .map(ChatRoomEntity::toDomain)
                .toList();
    }

    @Override
    public List<ChatMessage> findLatestMessagesByRoomNos(List<Long> roomNos) {
        return springJpaChatMessageRepository.findLatestMessagesByRoomNos(roomNos)
                .stream()
                .map(ChatMessageEntity::toDomain)
                .toList();
    }

    @Override
    public List<ChatMessage> findMessagesByRoomNo(Long chatRoomNo, Long lastMessageNo, Pageable pageable) {
        return springJpaChatMessageRepository.findByChatRoomNoOrderByChatMessageNoDesc(chatRoomNo,lastMessageNo,pageable)
                .stream().map(ChatMessageEntity::toDomain)
                .toList()
                ;
    }

    @Override
    public List<UnreadCountMapping> findUnreadCountsByMemberNoAndRoomNos(Long memberNo, List<Long> roomNoList) {
        return springJpaChatParticipantRepository.findUnreadCountsByMemberNoAndRoomNos(memberNo,roomNoList);
    }
}