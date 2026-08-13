package com.sunghyun.chat.adapter.out.persistence.repository;

import com.sunghyun.chat.adapter.out.persistence.entity.ChatMessageEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SpringJpaChatMessageRepository extends JpaRepository<ChatMessageEntity,Long> {
    boolean existsByChatRoomNo(Long chatRoomNo);

    @Query(value =
            "SELECT m.* FROM chat_message m " +
                    "WHERE m.chat_message_no IN (" +
                    "    SELECT MAX(chat_message_no) " +
                    "    FROM chat_message " +
                    "    WHERE chat_room_no IN :roomNos " +
                    "    GROUP BY chat_room_no" +
                    ")", nativeQuery = true)
    List<ChatMessageEntity> findLatestMessagesByRoomNos(@Param("roomNos") List<Long> roomNos);

    // 💡 firstViewableMessageNo 조건 추가
    @Query("SELECT m FROM ChatMessageEntity m " +
            "WHERE m.chatRoomNo = :chatRoomNo " +
            "AND m.chatMessageNo >= :firstViewableMessageNo " + // 내가 볼 수 있는 최초 시점 이상
            "AND (:lastMessageNo IS NULL OR m.chatMessageNo < :lastMessageNo) " + // 기존 스크롤 페이징 커서
            "ORDER BY m.chatMessageNo DESC")
    List<ChatMessageEntity> findByChatRoomNoAndFirstViewable(
            @Param("chatRoomNo") Long chatRoomNo,
            @Param("firstViewableMessageNo") Long firstViewableMessageNo,
            @Param("lastMessageNo") Long lastMessageNo,
            Pageable pageable
    );
}
