package com.sunghyun.chat.application.dto.res;

import com.sunghyun.chat.domain.room.ChatRoom;
import com.sunghyun.chat.domain.room.enums.ChatRoomType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class ChatRoomSelectResDto {
    private Long chatRoomNo;
    private ChatRoomType chatRoomType;
    private List<ChatParticipantSelectResDto> chatParticipants;
    private String createdDt;
    private String createdTm;

    // 💡 도메인 -> DTO 변환 메서드 추가
    public static ChatRoomSelectResDto fromDomain(ChatRoom chatRoom) {
        if (chatRoom == null) return null;

        List<ChatParticipantSelectResDto> participantDtos = null;
        if (chatRoom.getChatParticipants() != null) {
            participantDtos = chatRoom.getChatParticipants().stream()
                    .map(ChatParticipantSelectResDto::fromDomain)
                    .toList();
        }

        return ChatRoomSelectResDto.builder()
                .chatRoomNo(chatRoom.getChatRoomNo())
                .chatRoomType(chatRoom.getChatRoomType())
                .chatParticipants(participantDtos)
                .createdDt(chatRoom.getCreatedDt())
                .createdTm(chatRoom.getCreatedTm())
                .build();
    }
}