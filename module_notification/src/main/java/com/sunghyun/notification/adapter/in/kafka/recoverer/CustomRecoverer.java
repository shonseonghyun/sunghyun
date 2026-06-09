package com.sunghyun.notification.adapter.in.kafka.recoverer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.listener.ConsumerAwareRecordRecoverer;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomRecoverer implements ConsumerAwareRecordRecoverer {
    @Override
    public void accept(ConsumerRecord<?, ?> record, Exception exception) {
        accept(record, null, exception);
    }

    @Override
    public void accept(ConsumerRecord<?, ?> record, Consumer<?, ?> consumer, Exception exception) {
        log.info("CustomRecoverer 진입");
    }
}
