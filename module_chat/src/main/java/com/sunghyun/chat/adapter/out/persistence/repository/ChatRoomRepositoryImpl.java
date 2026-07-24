package com.sunghyun.chat.adapter.out.persistence.repository;

import com.sunghyun.chat.adapter.out.persistence.entity.ChatRoomEntity;
import com.sunghyun.chat.domain.dto.UnreadCountMapping;
import com.sunghyun.chat.domain.room.ChatRoom;
import com.sunghyun.chat.domain.room.enums.ChatRoomType;
import com.sunghyun.chat.domain.room.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ChatRoomRepositoryImpl implements ChatRoomRepository {
    private final SpringJpaChatRoomRepository springJpaChatRoomRepository;
    private final SpringJpaChatParticipantRepository springJpaChatParticipantRepository;

    @Override
    public ChatRoom save(ChatRoom chatRoom) {
        return springJpaChatRoomRepository.save(ChatRoomEntity.fromDomain(chatRoom))
                .toDomain();
    }

    @Override
    public Optional<ChatRoom> findChatRoomByChatRoomNo(Long chatRoomNo) {
        return springJpaChatRoomRepository.findChatRoomByChatRoomNo(chatRoomNo)
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
    public List<UnreadCountMapping> findUnreadCountsByMemberNoAndRoomNos(Long memberNo, List<Long> roomNoList) {
        return springJpaChatParticipantRepository.findUnreadCountsByMemberNoAndRoomNos(memberNo,roomNoList);
    }

    @Override
    public List<ChatRoom> findChatRoomsByMemberNo(Long memberNo) {
        return springJpaChatRoomRepository.findChatRoomByMemberNo(memberNo)
                .stream()
                .map(ChatRoomEntity::toDomain)
                .toList();
    }
}