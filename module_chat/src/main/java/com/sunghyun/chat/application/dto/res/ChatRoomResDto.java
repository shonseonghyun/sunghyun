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

    //상대방들 번호
    private List<Long> friendMembersNo;

    //총 인원 수
    private Integer participantCount;

    private Long lastSenderMemberNo; //이게 필요한가? 프론트 확인 필요.

    private LastMessageInfoResDto lastMessageInfo;

    private Integer unreadCount;
}