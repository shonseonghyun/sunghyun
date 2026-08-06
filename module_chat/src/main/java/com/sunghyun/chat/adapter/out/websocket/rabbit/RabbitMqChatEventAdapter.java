package com.sunghyun.chat.adapter.out.websocket.rabbit;

import com.sunghyun.chat.adapter.config.RabbitProperties;
import com.sunghyun.chat.application.dto.enums.ChatEventType;
import com.sunghyun.chat.application.dto.res.ChatRoomNoResDto;
import com.sunghyun.chat.application.dto.res.WebSocketResDto;
import com.sunghyun.chat.application.port.out.dto.ChatEvent;
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

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleMessageCreated(ChatEvent.MessageCreated event) {
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
}