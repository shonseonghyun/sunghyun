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
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ChatController {
    private final ChatUseCase chatUseCase;
    private final SimpMessagingTemplate messagingTemplate;


    @PostMapping("/chat/room/{friendMemberNo}")
    public GlobalResponse getOrCreateChatRoom(@PathVariable Long friendMemberNo, @AuthMember AuthMemberInfo authMemberInfo){
        Long memberNo = authMemberInfo.getMemberNo();
        ChatRoomCreateResDto result = chatUseCase.getOrCreateChatRoom(memberNo,friendMemberNo);
        return GlobalResponse.of(ErrorCode.S000,result);
    }

    @GetMapping("/chat/room/{chatRoomNo}/messages")
    public GlobalResponse getChatMessages(@PathVariable Long chatRoomNo, @RequestParam(required = false) Long lastMessageNo, @RequestParam(defaultValue = "5") int pageSize,@AuthMember AuthMemberInfo authMemberInfo){
        List<ChatMessageResDto> result = chatUseCase.getChatMessages(chatRoomNo, lastMessageNo,pageSize);
        return GlobalResponse.of(ErrorCode.S000,result);
    }

    @PostMapping("/chat/room/{chatRoomNo}/read")
    public GlobalResponse markAsRead(
            @PathVariable Long chatRoomNo,
            @RequestBody ChatReadReqDto payload,
            @AuthMember AuthMemberInfo authMemberInfo
    ) {
        Long memberNo = authMemberInfo.getMemberNo();
        ChatReadResDto result = chatUseCase.readChatMessage(chatRoomNo, memberNo, payload.getLastReadChatMessageNo());
        return GlobalResponse.of(ErrorCode.S000, result);
    }

    @GetMapping("/my/chat/rooms")
    public GlobalResponse getMyChatRooms(@AuthMember AuthMemberInfo authMemberInfo){
        List<ChatRoomResDto> result = chatUseCase.getMyChatRooms(authMemberInfo.getMemberNo());
        return GlobalResponse.of(ErrorCode.S000,result);
    }

    @MessageMapping("/chat/room/{chatRoomNo}/send") // 서버는 해당 경로에 발행한 메시지를 수신한다.
//    @SendTo("/sub/chat/room/{chatRoomNo}") // 서버는 해당 경로를 구독한 클라이언트에게 메시지를 응답한다.
    public void handleMessage(@DestinationVariable Long chatRoomNo, @Payload ChatMessageSendReqDto payload) {
        log.info(payload.toString());
        // 채팅 메시지 보내는 경우, 누가 보냈는지 dto 내부에 memberNo 필드가 있어 chatParticipantNo를 통해 업데이트 업데이트 가능하다.
        // 대신, 삳대방이 채팅방 메시지에 들어와 해당 메시지를 바로 확인했는지 안했는지는 어떻게 알 수 있을까?
        ChatMessageSendResDto result = chatUseCase.createChatMessage(chatRoomNo,payload);

        messagingTemplate.convertAndSend(
                "/sub/chat/room/" + chatRoomNo,
                GlobalResponse.of(ErrorCode.S000, result)
        );

        // 목록 갱신용 최소한의 시그널만 전송
        if (result.getReceiverMemberNo() != null) {
            // 💡 굳이 GlobalResponse나 메시지 본문을 보낼 필요 없이 이벤트 이름 정도만 보냅니다.
            Map<String, Object> eventSignal = Map.of(
                    "type", "NEW_MESSAGE",
                    "chatRoomNo", chatRoomNo
            );

            log.info("/sub/member들어왔냐?");

            messagingTemplate.convertAndSend("/sub/member/" + result.getReceiverMemberNo(), eventSignal);
        }
    }
}