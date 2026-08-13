package com.sunghyun.chat.application.port.in;

import com.sunghyun.chat.application.dto.req.ChatMessageSendReqDto;
import com.sunghyun.chat.application.dto.res.ChatMessageListResDto;
import com.sunghyun.chat.application.dto.res.ChatReadResDto;

public interface ChatMessageUseCase {
    ChatMessageListResDto getChatMessages(Long chatRoomNo, Long memberNo, Long lastMessageNo, int pageSize);
    void createChatMessage(Long chatRoomNo, Long senderMemberNo, ChatMessageSendReqDto reqDto);
    ChatReadResDto readChatMessage(Long chatRoomNo, Long memberNo, Long lastReadChatMessageNo);
}