package com.sunghyun.chat.adapter.out.persistence.entity;

import com.sunghyun.chat.domain.ChatParticipant;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Table(name = "chat_participant")
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatParticipantEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatParticipantNo;

    @Column
    private Long memberNo;

    //chat_room_no (FK 존재)

    @Column
    private boolean isLeft;

    // Domain -> Entity 변환
    public static ChatParticipantEntity fromDomain(ChatParticipant domain) {
        if (domain == null) return null;

        return ChatParticipantEntity.builder()
                .chatParticipantNo(domain.getChatParticipantNo())
                .memberNo(domain.getMemberNo())
                .isLeft(domain.isLeft())
                .build();
    }

    // Entity -> Domain 변환
    public ChatParticipant toDomain() {
        return ChatParticipant.builder()
                .ChatParticipantNo(this.chatParticipantNo)
                .memberNo(this.memberNo)
                .isLeft(this.isLeft)
                .build();
    }
}
