package com.sunghyun.chat.adapter.out.websocket;

import com.sunghyun.chat.adapter.config.WebSocketTopicProperties;
import com.sunghyun.chat.application.dto.enums.ChatEventType;
import com.sunghyun.chat.application.dto.res.ChatRoomNoResDto;
import com.sunghyun.chat.application.dto.res.WebSocketResDto;
import com.sunghyun.chat.application.port.out.dto.ChatEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;

@Component
@RequiredArgsConstructor
public class WebSocketChatEventListener {

    private final SimpMessagingTemplate messagingTemplate;
    private final WebSocketTopicProperties topicProperties;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleMessageCreated(ChatEvent.MessageCreated event) {
        ChatEventType eventType = ChatEventType.NEW_MESSAGE;
        List<Long> receiverMembersNo = event.result().getReceiverMembersNo();

        // 1. 방에 들어와 있는 사람들에게 발송
        messagingTemplate.convertAndSend(
                topicProperties.getRoomTopic(event.chatRoomNo()),
                WebSocketResDto.of(eventType, event.result())
        );

        // 2. 방 밖에 있는(목록만 보는) 수신자들에게 새 알림 발송
        if (receiverMembersNo != null && !receiverMembersNo.isEmpty()) {
            for (Long receiverMemberNo : receiverMembersNo) {
                messagingTemplate.convertAndSend(
                        topicProperties.getMemberTopic(receiverMemberNo),
                        WebSocketResDto.of(eventType, new ChatRoomNoResDto(event.chatRoomNo()))
                );
            }
        }
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleMessageReaded(ChatEvent.MessageRead event) {
        ChatEventType eventType = ChatEventType.READ_UPDATE;

        // 방에 들어와있는 사람들에게 읽었다고 알림
        messagingTemplate.convertAndSend(
                topicProperties.getRoomTopic(event.chatRoomNo()),
                WebSocketResDto.of(eventType, event.result())
        );
    }
}