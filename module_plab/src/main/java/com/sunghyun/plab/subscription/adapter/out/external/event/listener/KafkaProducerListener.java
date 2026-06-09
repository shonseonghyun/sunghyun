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
        // 참고로 트랜잭셔널이벤트리스너 비동기 실행하면서 생성된 독립적인 스레드에서 처리됨
        NotificationRequestedEvent event = producerRecord.value();
        final String outBoxNo = event.getOutBoxNo();

        if (isNonRetryableException(exception)) {
            log.error("[영구 실패] 개발자 긴급 확인 필요! 재시도 불가 에러 발생. ID=[{}]", outBoxNo, exception);
            // 슬랙 알림 발송 로직 추가 가능 (slackWebhook.send(...))

//            outBoxCommandService.updateToPermanentFailed(outBoxNo); // 영구 실패 상태 마킹
        }
        // 💡 2. 회복 가능한 일시적 예외
        else {
            log.warn("[일시적 실패] 재시도 위해 상태값 변경. ID=[{}]", outBoxNo, exception);
            outBoxCommandService.updateStatus(outBoxNo, false); // 일반 FAILED 상태 마킹 (스케줄러가 주워갈 타겟)
        }
    }

    private boolean isNonRetryableException(Exception ex) {
        return ex instanceof org.apache.kafka.common.errors.SerializationException ||
                ex instanceof org.apache.kafka.common.errors.RecordTooLargeException ||
                ex instanceof org.apache.kafka.common.errors.InvalidTopicException
                ;
    }
}
