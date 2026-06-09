package com.sunghyun.notification.adapter.in.kafka.handler;

import com.sunghyun.web.exception.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.listener.ConsumerRecordRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.backoff.FixedBackOff;

@Slf4j
@Component
public class CustomErrorHandler extends DefaultErrorHandler {

    public CustomErrorHandler(ConsumerRecordRecoverer recoverer) {
        super(recoverer, new FixedBackOff(1000L,2));

        this.setCommitRecovered(true);

        // 복구 불가 예외 필터링 규칙 정의
        this.addNotRetryableExceptions(BaseException.class);
    }
}
