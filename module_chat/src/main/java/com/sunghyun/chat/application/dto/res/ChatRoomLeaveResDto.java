package com.sunghyun.chat.application.dto.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
@Builder
public class ChatRoomLeaveResDto {
    private List<Long> receiverMembersNo;

}
