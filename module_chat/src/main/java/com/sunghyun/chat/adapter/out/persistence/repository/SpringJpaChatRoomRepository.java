package com.sunghyun.chat.adapter.out.persistence.repository;

import com.sunghyun.chat.adapter.out.persistence.entity.ChatRoomEntity;
import com.sunghyun.chat.domain.enums.ChatRoomType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface SpringJpaChatRoomRepository extends JpaRepository<ChatRoomEntity, Long> {
    @Query("SELECT DISTINCT cr FROM ChatRoomEntity cr " +
            "JOIN FETCH cr.chatParticipants " +
            "WHERE cr.chatRoomType = :roomType " +
            "AND EXISTS (SELECT p FROM cr.chatParticipants p WHERE p.memberNo = :myMemberNo) " +
            "AND EXISTS (SELECT p FROM cr.chatParticipants p WHERE p.memberNo = :friendMemberNo)")
    Optional<ChatRoomEntity> findChatRoomByMemberNosAndChatRoomType(
            @Param("myMemberNo") Long myMemberNo,
            @Param("friendMemberNo") Long friendMemberNo,
            @Param("roomType") ChatRoomType roomType
    );

    @Query("SELECT DISTINCT cr FROM ChatRoomEntity cr " +
            "JOIN FETCH cr.chatParticipants " + // 참여자 데이터 다 긁어오기 (N+1 방지)
            "JOIN cr.chatParticipants cp " +    // 내 조건 걸기용 조인
            "WHERE cr.chatRoomType = :roomType " +
            "AND cp.memberNo = :myMemberNo")
    List<ChatRoomEntity> findChatRoomsByMemberNoAndChatRoomType(
            @Param("myMemberNo") Long myMemberNo,
            @Param("roomType") ChatRoomType roomType
    );
}