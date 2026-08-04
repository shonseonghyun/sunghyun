package com.sunghyun.chat.application.dto.req;

import com.sunghyun.chat.domain.room.enums.ChatRoomType;
import lombok.Getter;

import java.util.List;

@Getter
public class ChatRoomCreateReqDto {
    ChatRoomType roomType;
    List<Long> targetMemberNos;
}
