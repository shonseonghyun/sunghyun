package com.sunghyun.chat.application.dto.req;

import lombok.Getter;

import java.util.List;

@Getter
public class ChatRoomInviteReqDto {
    private Long chatRoomNo;
    private List<Long> targetMemberNos;
}