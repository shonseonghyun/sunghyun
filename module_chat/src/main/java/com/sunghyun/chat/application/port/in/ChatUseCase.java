package com.sunghyun.chat.application.port.in;

import com.sunghyun.chat.application.dto.*;

import java.util.List;

public interface ChatUseCase {
    ChatRoomCreateResDto getOrCreateChatRoom(Long memberNo, Long friendMemberNo);
    List<ChatRoomResDto> getMyChatRooms(Long memberNo);
    ChatMessageSendResDto createChatMessage(Long chatRoomNo, ChatMessageSendReqDto payload);
    List<ChatMessageResDto> getChatMessages(Long chatRooNo, Long lastMessageNo, int pageSize);
    ChatReadResDto readChatMessage(Long chatRoomNo, Long memberNo, Long lastReadChatMessageNo);
}
