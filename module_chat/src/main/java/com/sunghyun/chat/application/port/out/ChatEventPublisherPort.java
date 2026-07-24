package com.sunghyun.chat.application.port.out;

import com.sunghyun.chat.application.port.out.dto.ChatEvent;

public interface ChatEventPublisherPort {
    void publishChatMessageCreated(ChatEvent.MessageCreated event);
    void publishReadMessageUpdated(ChatEvent.MessageRead event);
}
