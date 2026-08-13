package com.sunghyun.chat.application.port.in;

import com.sunghyun.chat.application.dto.req.ChatRoomCreateReqDto;
import com.sunghyun.chat.application.dto.req.ChatRoomInviteReqDto;
import com.sunghyun.chat.application.dto.res.*;

import java.util.List;

public interface ChatRoomUseCase {
    ChatRoomSelectResDto getChatRoom(Long chatRoomNo, Long memberNo);
    ChatRoomCreateResDto getOrCreateChatRoom(Long memberNo, ChatRoomCreateReqDto reqDto);
    List<ChatRoomResDto> getMyChatRooms(Long memberNo);
    void leaveMember(Long chatRoomNo, Long memberNo);
    ChatRoomCreateResDto inviteMember(Long chatRoomNo, Long memberNo, ChatRoomInviteReqDto reqDto);
}