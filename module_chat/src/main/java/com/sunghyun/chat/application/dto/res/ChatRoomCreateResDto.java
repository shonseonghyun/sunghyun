package com.sunghyun.chat.application.dto.res;

import com.sunghyun.chat.domain.room.enums.ChatRoomType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ChatRoomCreateResDto {
    private Long chatRoomNo;
    private ChatRoomType roomType;
}
