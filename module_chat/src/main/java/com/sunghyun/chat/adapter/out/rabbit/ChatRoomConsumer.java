package com.sunghyun.chat.adapter.out.rabbit;

import com.sunghyun.chat.adapter.config.RabbitProperties;
import com.sunghyun.chat.application.dto.enums.ChatEventType;
import com.sunghyun.chat.application.dto.res.WebSocketResDto;
import com.sunghyun.chat.application.port.out.dto.ChatEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@RabbitListener(queues = "${rabbitmq.queue.chat}") // chat.room.queue
public class ChatRoomConsumer {
    private final SimpMessagingTemplate messagingTemplate;
    private final RabbitProperties rabbitProperties;

    @RabbitHandler
    public void handleMessageCreated(ChatEvent.MessageCreated event) {
        log.info("RabbitMQ 수신 [채팅방 브로드캐스트] - 방 번호: {}", event.chatRoomNo());
        log.info("[{}]",rabbitProperties.getRoomRoutingKey(event.chatRoomNo()));
        messagingTemplate.convertAndSend(
                rabbitProperties.getRoomRoutingKey(event.chatRoomNo()),
                WebSocketResDto.of(ChatEventType.NEW_MESSAGE, event.result())
        );
    }

    @RabbitHandler
    public void handleMessageRead(ChatEvent.MessageRead event) {
        log.info("RabbitMQ 수신 [읽음 처리 브로드캐스트] - 방 번호: {}", event.chatRoomNo());

        messagingTemplate.convertAndSend(
                rabbitProperties.getRoomRoutingKey(event.chatRoomNo()),
                WebSocketResDto.of(ChatEventType.READ_UPDATE, event.result())
        );
    }
}