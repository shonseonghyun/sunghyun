package com.sunghyun.chat.application.dto.res;

import com.sunghyun.chat.domain.message.enums.ChatMessageType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
@AllArgsConstructor
public class LastMessageInfoResDto {
    private String content;
    private ChatMessageType messageType;
    private String sendDt;
    private String sendTm;
}
