package com.sunghyun.chat.adapter.out.persistence.entity;

import com.sunghyun.chat.domain.room.ChatRoom;
import com.sunghyun.chat.domain.room.enums.ChatRoomType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Table(name = "chat_room")
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatRoomNo;

    @Column(name = "room_type")
    @Enumerated(EnumType.STRING)
    private ChatRoomType chatRoomType;

    @OneToMany(cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    @JoinColumn(name = "chat_room_no",updatable = false,nullable = false)
    private List<ChatParticipantEntity> chatParticipants;

    @Column
    private String createdDt;

    @Column
    private String createdTm;

    // Domain -> Entity 변환
    public static ChatRoomEntity fromDomain(ChatRoom domain) {
        if (domain == null) return null;

        List<ChatParticipantEntity> participantEntities = new ArrayList<>();
        if (domain.getChatParticipants() != null) {
            participantEntities = domain.getChatParticipants().stream()
                    .map(ChatParticipantEntity::fromDomain)
                    .collect(Collectors.toList());
        }

        return ChatRoomEntity.builder()
                .chatRoomNo(domain.getChatRoomNo())
                .chatRoomType(domain.getChatRoomType())
                .chatParticipants(participantEntities)
                .createdDt(domain.getCreatedDt())
                .createdTm(domain.getCreatedTm())
                .build();
    }

    // Entity -> Domain 변환
    public ChatRoom toDomain() {
        return ChatRoom.builder()
                .chatRoomNo(this.chatRoomNo)
                .chatRoomType(this.chatRoomType)
                .chatParticipants(
                        this.chatParticipants == null ? null :
                                this.chatParticipants.stream()
                                        .map(ChatParticipantEntity::toDomain)
                                        .toList()
                )
                .createdDt(this.createdDt)
                .createdTm(this.createdTm)
                .build();
    }
}