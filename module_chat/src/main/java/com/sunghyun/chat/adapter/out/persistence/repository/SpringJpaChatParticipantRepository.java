package com.sunghyun.chat.adapter.out.persistence.repository;

import com.sunghyun.chat.adapter.out.persistence.entity.ChatParticipantEntity;
import com.sunghyun.chat.domain.dto.UnreadCountMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SpringJpaChatParticipantRepository extends JpaRepository<ChatParticipantEntity, Long> {

    @Query(value =
            "SELECT cm.chat_room_no AS chatRoomNo, COUNT(cm.chat_message_no) AS unreadCount " +
                    "FROM chat_participant cp " +
                    "JOIN chat_message cm ON cp.chat_room_no = cm.chat_room_no " +
                    "WHERE cp.member_no = :memberNo " +
                    "  AND cp.chat_room_no IN :roomNoList " +
                    "  AND cm.chat_message_no > COALESCE(cp.last_read_chat_message_no, 0) " +
                    "GROUP BY cm.chat_room_no",
            nativeQuery = true) // 👈 nativeQuery = true
    List<UnreadCountMapping> findUnreadCountsByMemberNoAndRoomNos(
            @Param("memberNo") Long memberNo,
            @Param("roomNoList") List<Long> roomNoList
    );
}