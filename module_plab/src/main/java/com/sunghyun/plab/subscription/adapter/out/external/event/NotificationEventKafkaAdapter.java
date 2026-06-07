//package com.sunghyun.plab.subscription.adapter.out.external;
//
//import com.sunghyun.plab.subscription.application.port.out.dto.NotificationRequestedEvent;
//import com.sunghyun.plab.subscription.application.port.out.external.NotificationEventOutPort;
//import lombok.RequiredArgsConstructor;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.stereotype.Component;
//
//@Component
//@RequiredArgsConstructor
//public class NotificationEventKafkaAdapter implements NotificationEventOutPort {
//    private final KafkaTemplate<String,Object> kafkaTemplate;
//    private final static String topic = "plab-noti";
//
//    @Override
//    public void publish(final NotificationRequestedEvent requestedEvent) {
//        kafkaTemplate.send(topic,requestedEvent);
//    }
//}
