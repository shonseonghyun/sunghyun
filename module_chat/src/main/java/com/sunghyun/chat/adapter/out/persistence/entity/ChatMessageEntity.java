package com.sunghyun.chat.adapter.out.persistence.entity;

import com.sunghyun.chat.domain.message.ChatMessage;
import com.sunghyun.chat.domain.message.enums.ChatMessageType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Table(
        name = "chat_message"
//        ,indexes = {
//                @Index(name = "idx_chatroom_message", columnList = "chat_room_no, chat_message_no DESC")
//        } // 💡 20개씩 최신순 페이징 조회를 위한 인덱스 최적화
)
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatMessageNo;

    @Column(name = "chat_room_no", nullable = false)
    // ChatPartitcipantEntity와 생명주기가 다르기에 연관관계 매핑하지 않는다.
    private Long chatRoomNo;

    @Column(name = "member_no", nullable = false)
    private Long senderMemberNo;

    @Column(columnDefinition = "TEXT", nullable = false) // 장문의 텍스트나 포스트 데이터 수용
    private String content;

    @Column(name = "message_type")
    @Enumerated(EnumType.STRING)
    private ChatMessageType messageType;

    @Column
    private String sendDt;

    @Column
    private String sendTm;

    public static ChatMessageEntity fromDomain(ChatMessage domain) {
        if (domain == null) return null;

        return ChatMessageEntity.builder()
                .chatMessageNo(domain.getChatMessageNo())
                .chatRoomNo(domain.getChatRoomNo())
                .senderMemberNo(domain.getSenderMemberNo())
                .content(domain.getContent())
                .messageType(domain.getMessageType())
                .sendDt(domain.getSendDt())
                .sendTm(domain.getSendTm())
                .build();
    }

    public ChatMessage toDomain() {
        return ChatMessage.builder()
                .chatMessageNo(this.chatMessageNo)
                .chatRoomNo(this.chatRoomNo)
                .senderMemberNo(this.senderMemberNo)
                .content(this.content)
                .messageType(this.messageType)
                .sendDt(this.sendDt)
                .sendTm(this.sendTm)
                .build();
    }
}