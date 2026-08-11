package com.sunghyun.chat.application.dto.res;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sunghyun.chat.domain.room.enums.ChatRoomType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChatRoomResDto {
    private Long chatRoomNo;

    private ChatRoomType chatRoomType;

    //총 인원 수
    private Integer participantCount;

    // 채팅에 참여한 회원 간단 정보
    private List<ChatMemberInfo> chatMembers;

    private LastMessageInfoResDto lastMessageInfo;

    private Integer unreadCount;
}