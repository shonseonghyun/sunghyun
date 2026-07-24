package com.sunghyun.chat.adapter.out.event;

import com.sunghyun.chat.application.port.out.ChatEventPublisherPort;
import com.sunghyun.chat.application.port.out.dto.ChatEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SpringChatEventPublisherAdapter implements ChatEventPublisherPort {
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public void publishChatMessageCreated(ChatEvent.MessageCreated event) {
        eventPublisher.publishEvent(event);
    }

    @Override
    public void publishReadMessageUpdated(ChatEvent.MessageRead event) {
        eventPublisher.publishEvent(event);
    }
}
