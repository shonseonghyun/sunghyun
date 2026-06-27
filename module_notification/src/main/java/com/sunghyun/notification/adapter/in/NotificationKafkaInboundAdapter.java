package com.sunghyun.notification.adapter.in;

import com.sunghyun.notification.application.port.in.NotificationUseCase;
import com.sunghyun.notification.application.port.in.dto.NotificationRequestEventDto;
import com.sunghyun.web.ErrorCode;
import com.sunghyun.web.exception.BaseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationKafkaInboundAdapter {
    private final NotificationUseCase notificationUseCase;

    @KafkaListener(topics = "plab.noti.subscription",groupId = "plab-noti-group")
    public void handleNotificationEvent(final NotificationRequestEventDto dto, final Acknowledgment ack){
        log.info("[Kafka Adapter] 메시지 수신 완료. 토픽: plab-noti, 데이터:[{}]",dto);

        if(true) throw new BaseException(ErrorCode.F000);

        notificationUseCase.doNoti(dto);

        ack.acknowledge();
    }

//    @KafkaListener(topics = "plab.noti.subscription.DLT")
//    public void handleDlt(final NotificationRequestEventDto dto, final Acknowledgment ack){
//        log.info("[Kafka Adapter] DLT 메시지 수신 완료. 토픽: plab.noti.subscription.DLT, 데이터:[{}]",dto);
//    }
}
