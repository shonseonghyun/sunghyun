//package com.sunghyun.plab.subscription.adapter.out.external;
//
//import com.sunghyun.plab.subscription.application.port.out.dto.NotificationRequestedEvent;
//import com.sunghyun.plab.subscription.application.port.out.external.NotificationEventOutPort;
//import lombok.RequiredArgsConstructor;
//import org.springframework.context.ApplicationEventPublisher;
//import org.springframework.stereotype.Component;
//
//@Component
//@RequiredArgsConstructor
//public class NotificationEventSpringEventListenerAdapter implements NotificationEventOutPort {
//    private final ApplicationEventPublisher eventPublisher;
//
//    @Override
//    public void publish(final NotificationRequestedEvent requestedEvent) {
//            eventPublisher.publishEvent(requestedEvent);
//    }
//}
