package com.sunghyun.chat.application.dto.req;

import com.sunghyun.chat.domain.enums.ChatMessageType;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ChatMessageSendReqDto {
    private Long chatMessageNo;

    private Long senderMemberNo;
    private Long receiverMemberNo;

    private String name;
    private ChatMessageType messageType;
    private String content;
}
