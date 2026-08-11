package com.sunghyun.chat.adapter.out.websocket.rabbit;

import com.sunghyun.chat.adapter.config.RabbitProperties;
import com.sunghyun.chat.application.dto.enums.ChatEventType;
import com.sunghyun.chat.application.dto.res.ChatRoomNoResDto;
import com.sunghyun.chat.application.dto.res.WebSocketResDto;
import com.sunghyun.chat.application.port.out.dto.ChatEvent;
import com.sunghyun.chat.domain.message.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class RabbitMqChatEventAdapter {
    private final RabbitMessagingTemplate rabbitMessagingTemplate;
    private final RabbitProperties rabbitProperties;
    private final ChatMessageRepository chatMessageRepository;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleInvitedChatRoom(ChatEvent.ChatRoomInvited event) {
        /*
                            초대 알림 기준
                        개별 방 | 채팅 목록(memberNoList필요)
            단체 채팅방 ->   O       X
         */
        rabbitMessagingTemplate.convertAndSend(
                rabbitProperties.getExchange().getName(),
                rabbitProperties.getRoomRoutingKey(event.chatRoomNo()),
                WebSocketResDto.of(ChatEventType.CHAT_ROOM_INVITED, event.result())
        );
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleMessageCreated(ChatEvent.MessageCreated event) {
        /*
                            메시자 발송 알림 기준
                        개별 방 | 채팅 목록(memberNoList필요)
            개인 채팅방 ->   O       O
            단체 채팅방 ->   O       O
         */
        // 1. [채팅방 전송용]
        final String roomRoutingKey = rabbitProperties.getRoomRoutingKey(event.chatRoomNo());
        log.info("RabbitMQ 발행 [채팅방 메시지] - routingKey: {}", roomRoutingKey);
        // exchange로 보낸다.(exchange는 "나랑 바인딩(연결)되어 있는 모든 큐(Queue)를 찾아서 메시지를 복사해 다 뿌려야겠다")
        rabbitMessagingTemplate.convertAndSend(
                rabbitProperties.getExchange().getName(),
                rabbitProperties.getRoomRoutingKey(event.chatRoomNo()),
                WebSocketResDto.of(ChatEventType.NEW_MESSAGE, event.result())
        );

        // 2. [개별 멤버 알림용]
        List<Long> receiverMembersNo = event.result().getReceiverMembersNo();
        if (receiverMembersNo != null && !receiverMembersNo.isEmpty()) {
            log.info("RabbitMQ 발행 [개별 멤버 알림] - 수신자 수: {}", receiverMembersNo.size());

            for(Long receiverMemberNo:receiverMembersNo){
                rabbitMessagingTemplate.convertAndSend(
                        rabbitProperties.getExchange().getName(),
                        rabbitProperties.getMemberRoutingKey(receiverMemberNo),
                        WebSocketResDto.of(ChatEventType.NEW_MESSAGE, new ChatRoomNoResDto(event.chatRoomNo()))
                );

            }
//            log.info("RabbitMQ 발행 [멤버 알림 통합] - routingKey: {}", memberRoutingKey);
        }
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleMessageReaded(ChatEvent.MessageRead event) {
        // 3. [읽음 처리용]
        final String roomRoutingKey = rabbitProperties.getRoomRoutingKey(event.chatRoomNo());
        log.info("RabbitMQ 발행 [읽음 처리] - routingKey: {}", roomRoutingKey);

        rabbitMessagingTemplate.convertAndSend(
                rabbitProperties.getExchange().getName(),
                rabbitProperties.getRoomRoutingKey(event.chatRoomNo()),
                WebSocketResDto.of(ChatEventType.READ_UPDATE, event.result())
        );
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleLeftChatRoom(ChatEvent.ChatRoomLeaved event) {
        /*
            "..님이 채팅방을 나갔습니다" 알림 기준
                        개별 방 | 채팅 목록
            개인 채팅방 ->   X       X
            단체 채팅방 ->   O       X
         */
        // [채팅방 전송용]
        //  해당 메시지를 구독받은 클라이언트는 채팅방 메시지들에 대해 재요청해야한다.
        rabbitMessagingTemplate.convertAndSend(
                rabbitProperties.getExchange().getName(),
                rabbitProperties.getRoomRoutingKey(event.chatRoomNo()),
                WebSocketResDto.of(ChatEventType.CHAT_ROOM_LEFT, event.result())
        );

        // [개별 멤버 알림용]
        // 채탕방 목록에 있는 개인 회원들에게 알림
//        List<Long> receiverMembersNo = event.result().getReceiverMembersNo();
//        if (receiverMembersNo != null && !receiverMembersNo.isEmpty()) {
//            for(Long receiverMemberNo:receiverMembersNo){
//                rabbitMessagingTemplate.convertAndSend(
//                        rabbitProperties.getExchange().getName(),
//                        rabbitProperties.getMemberRoutingKey(receiverMemberNo),
//                        // 이걸 응답받으면 프론트엔ㄷ
//                        WebSocketResDto.of(ChatEventType.CHAT_ROOM_LEFT, new ChatRoomNoResDto(event.chatRoomNo()))
//                );
//
//            }
//        }
    }
}