package com.sunghyun.chat.application.dto.res;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ChatMessageListResDto {
    private List<ChatMessageResDto> messages;
    private List<ChatParticipantLastReadResDto> participants;
}