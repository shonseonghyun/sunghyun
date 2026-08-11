package com.sunghyun.chat.application.port.in;

import com.sunghyun.chat.application.dto.req.ChatMessageSendReqDto;
import com.sunghyun.chat.application.dto.req.ChatRoomCreateReqDto;
import com.sunghyun.chat.application.dto.req.ChatRoomInviteReqDto;
import com.sunghyun.chat.application.dto.res.ChatMessageListResDto;
import com.sunghyun.chat.application.dto.res.ChatReadResDto;
import com.sunghyun.chat.application.dto.res.ChatRoomCreateResDto;
import com.sunghyun.chat.application.dto.res.ChatRoomResDto;

import java.util.List;

public interface ChatUseCase {
    ChatRoomCreateResDto getOrCreateChatRoom(Long memberNo, ChatRoomCreateReqDto reqDto);
    List<ChatRoomResDto> getMyChatRooms(Long memberNo);
    void createChatMessage(Long chatRoomNo, Long senderMemberNo, ChatMessageSendReqDto reqDto);
    ChatMessageListResDto getChatMessages(Long chatRoomNo, Long lastMessageNo, int pageSize);
    ChatReadResDto readChatMessage(Long chatRoomNo, Long memberNo, Long lastReadChatMessageNo);
    void leaveMember(Long chatRoomNo, Long memberNo);
    ChatRoomCreateResDto inviteMember(Long chatRoomNo, Long memberNo, ChatRoomInviteReqDto reqDto);
}