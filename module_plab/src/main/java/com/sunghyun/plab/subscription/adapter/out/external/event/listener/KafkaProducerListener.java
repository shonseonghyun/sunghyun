package com.sunghyun.plab.subscription.adapter.out.external.event.listener;

import com.sunghyun.plab.subscription.application.OutBoxCommandService;
import com.sunghyun.plab.subscription.application.port.out.dto.NotificationRequestedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.support.ProducerListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaProducerListener implements ProducerListener<String, NotificationRequestedEvent> {
    private final OutBoxCommandService outBoxCommandService;

    @Override
    public void onSuccess(
            ProducerRecord<String, NotificationRequestedEvent> producerRecord,
            RecordMetadata recordMetadata)
    {

        // 💡 이제 캐스팅 없이 안전하고 깔끔하게 꺼내 쓸 수 있습니다.
        NotificationRequestedEvent event = producerRecord.value();
        final String outBoxNo = event.getOutBoxNo();

        log.info("카프카 전송 성공! 아웃박스 ID=[{}]", outBoxNo);

        outBoxCommandService.updateStatus(outBoxNo, true);
    }

    @Override
    public void onError(
            ProducerRecord<String, NotificationRequestedEvent> producerRecord,
            RecordMetadata recordMetadata,
            Exception exception)
    {
        NotificationRequestedEvent event = producerRecord.value();
        final String outBoxNo = event.getOutBoxNo();

        log.error("카프카 전송 실패! 아웃박스 ID=[{}]", outBoxNo, exception);

        outBoxCommandService.updateStatus(outBoxNo, false);
    }
}
