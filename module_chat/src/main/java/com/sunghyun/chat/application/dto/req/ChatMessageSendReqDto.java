package com.sunghyun.chat.application.dto.req;

import com.sunghyun.chat.domain.message.enums.ChatMessageType;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ChatMessageSendReqDto {
    private Long chatMessageNo;

    private String senderName;
    private Long senderMemberNo;

    private ChatMessageType messageType;
    private String content;
}
