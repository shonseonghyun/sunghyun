package com.sunghyun.chat.adapter.out.rabbit;

import com.sunghyun.chat.adapter.config.RabbitProperties;
import com.sunghyun.chat.application.dto.enums.ChatEventType;
import com.sunghyun.chat.application.dto.res.ChatRoomNoResDto;
import com.sunghyun.chat.application.dto.res.WebSocketResDto;
import com.sunghyun.chat.application.port.out.dto.ChatEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
@RabbitListener(queues = "${rabbitmq.queue.member}")
public class MemberNotificationConsumer {
    private final SimpMessagingTemplate messagingTemplate;
    private final RabbitProperties rabbitProperties;

    @RabbitHandler
    public void handleMessageCreated(ChatEvent.MessageCreated event) {
        log.info("RabbitMQ 수신 [개별 멤버 알림] - 방 번호: {}", event.chatRoomNo());

        List<Long> receiverMembersNo = event.result().getReceiverMembersNo();
        if (receiverMembersNo != null && !receiverMembersNo.isEmpty()) {
            // 💡 컨슈머에서만 유일하게 루프를 돌며 각 회원에게 웹소켓 전송
            for (Long receiverMemberNo : receiverMembersNo) {
                log.info("수신자들[{}]에게 개별 알림 [{}]",receiverMemberNo,rabbitProperties.getMemberRoutingKey(receiverMemberNo));
                messagingTemplate.convertAndSend(
                        rabbitProperties.getMemberRoutingKey(receiverMemberNo),
                        WebSocketResDto.of(ChatEventType.NEW_MESSAGE, new ChatRoomNoResDto(event.chatRoomNo()))
                );
            }
        }
    }
}