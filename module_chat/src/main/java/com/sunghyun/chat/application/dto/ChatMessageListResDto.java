package com.sunghyun.chat.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ChatMessageListResDto {
    private List<ChatMessageResDto> messages;
    private List<ChatParticipantResDto> participants;
}