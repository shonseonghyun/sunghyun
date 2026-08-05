package com.sunghyun.chat.adapter.out.websocket;

import com.sunghyun.chat.adapter.config.RabbitProperties;
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
public class WebSocketChatEventListener {
    private final RabbitMessagingTemplate rabbitMessagingTemplate;
    private final RabbitProperties rabbitProperties;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleMessageCreated(ChatEvent.MessageCreated event) {
        // 1. [채팅방 전송용]
        final String roomRoutingKey = rabbitProperties.getRoomRoutingKey(event.chatRoomNo());
        log.info("RabbitMQ 발행 [채팅방] - routingKey: {}", roomRoutingKey);

        rabbitMessagingTemplate.convertAndSend(
                rabbitProperties.getExchange().getName(),
                roomRoutingKey,
                event
        );

        // 2. [개별 멤버 알림용]
        List<Long> receiverMembersNo = event.result().getReceiverMembersNo();
        if (receiverMembersNo != null && !receiverMembersNo.isEmpty()) {
            String memberRoutingKey = rabbitProperties.getRouting().getMember().getPrefix() + ".alarm";

            log.info("RabbitMQ 발행 [멤버 알림 통합] - routingKey: {}", memberRoutingKey);
            rabbitMessagingTemplate.convertAndSend(
                    rabbitProperties.getExchange().getName(),
                    memberRoutingKey,
                    event
            );
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
                roomRoutingKey,
                event
        );
    }
}