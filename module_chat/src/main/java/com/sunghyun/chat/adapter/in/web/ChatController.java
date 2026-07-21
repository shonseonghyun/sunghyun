package com.sunghyun.chat.adapter.in.web;

import com.sunghyun.annotation.AuthMember;
import com.sunghyun.chat.application.dto.*;
import com.sunghyun.chat.application.port.in.ChatUseCase;
import com.sunghyun.dto.AuthMemberInfo;
import com.sunghyun.web.ErrorCode;
import com.sunghyun.web.GlobalResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ChatController {
    private final ChatUseCase chatUseCase;

    @PostMapping("/chat/room/{friendMemberNo}")
    public GlobalResponse getOrCreateChatRoom(@PathVariable Long friendMemberNo, @AuthMember AuthMemberInfo authMemberInfo){
        Long memberNo = authMemberInfo.getMemberNo();
        ChatRoomCreateResDto result = chatUseCase.getOrCreateChatRoom(memberNo,friendMemberNo);
        return GlobalResponse.of(ErrorCode.S000,result);
    }

    @GetMapping("/chat/room/{chatRoomNo}/messages")
    public GlobalResponse getChatMessages(@PathVariable Long chatRoomNo, @RequestParam(required = false) Long lastMessageNo, @RequestParam(defaultValue = "5") int pageSize){
        List<ChatMessageResDto> result = chatUseCase.getChatMessages(chatRoomNo,lastMessageNo,pageSize);
        return GlobalResponse.of(ErrorCode.S000,result);
    }

    @GetMapping("/my/chat/rooms")
    public GlobalResponse getMyChatRooms(@AuthMember AuthMemberInfo authMemberInfo){
        List<ChatRoomResDto> result = chatUseCase.getMyChatRooms(authMemberInfo.getMemberNo());
        return GlobalResponse.of(ErrorCode.S000,result);
    }

    @MessageMapping("/chat/room/{chatRoomNo}/send") // 서버는 해당 경로에 발행한 메시지를 수신한다.
    @SendTo("/sub/chat/room/{chatRoomNo}") // 서버는 해당 경로를 구독한 클라이언트에게 메시지를 응답한다.
    public GlobalResponse handleMessage(@DestinationVariable Long chatRoomNo, @Payload ChatMessageSendReqDto payload) {
        ChatMessageSendResDto result = chatUseCase.createChatMessage(chatRoomNo,payload);
        return GlobalResponse.of(ErrorCode.S000,result);
    }
}
