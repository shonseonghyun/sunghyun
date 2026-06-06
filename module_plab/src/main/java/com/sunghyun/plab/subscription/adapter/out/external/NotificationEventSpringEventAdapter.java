package com.sunghyun.plab.subscription.adapter.out.external;

import com.sunghyun.plab.subscription.application.port.out.dto.NotificationRequestedEvent;
import com.sunghyun.plab.subscription.application.port.out.external.NotificationEventOutPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Slf4j
@Primary
@Component
@RequiredArgsConstructor
public class NotificationEventSpringEventAdapter implements NotificationEventOutPort {
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public void publish(final NotificationRequestedEvent requestedEvent) {
        log.info("NotificationRequestedEvent 이벤트 발행");
        eventPublisher.publishEvent(requestedEvent);
    }
}
