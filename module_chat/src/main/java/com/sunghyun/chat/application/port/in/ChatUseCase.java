package com.sunghyun.chat.application.port.in;

import com.sunghyun.chat.application.dto.req.ChatMessageSendReqDto;
import com.sunghyun.chat.application.dto.res.*;

import java.util.List;

public interface ChatUseCase {
    ChatRoomCreateResDto getOrCreateChatRoom(Long memberNo, Long friendMemberNo);
    List<ChatRoomResDto> getMyChatRooms(Long memberNo);
    ChatMessageSendResDto createChatMessage(Long chatRoomNo, Long senderMemberNo, ChatMessageSendReqDto payload);
    ChatMessageListResDto getChatMessages(Long chatRoomNo, Long lastMessageNo, int pageSize);
    ChatReadResDto readChatMessage(Long chatRoomNo, Long memberNo, Long lastReadChatMessageNo);
}