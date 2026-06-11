package com.sunghyun.notification.adapter.in.kafka.recoverer;

import com.sunghyun.notification.application.port.in.NotificationUseCase;
import com.sunghyun.notification.application.port.in.dto.NotificationRequestEventDto;
import com.sunghyun.web.exception.BaseException;
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
    private final NotificationUseCase notificationUseCase;

    @Override
    public void accept(ConsumerRecord<?, ?> record, Exception exception) {
        accept(record, null, exception);
    }

    @Override
    public void accept(ConsumerRecord<?, ?> record, Consumer<?, ?> consumer, Exception exception) {
        log.info("CustomRecoverer 진입 - 스레드 확인");

        Throwable rootCause =getRootCause(exception);

        String exceptionName = rootCause != null ? rootCause.getClass().getSimpleName() : "UnknownException";
        String exceptionMessage = rootCause != null ? rootCause.getMessage() : "No message available";

        final String topic = record.topic();
        final int partition = record.partition();
        final long offset = record.offset();
        final String key = record.key() != null ? record.key().toString() : "null";
        final Object message = record.value() != null ? record.value().toString() : "null";

        final String subject = String.format(
                "[위험] 카프카 복구 불가 예외 발생 (%s)",
                exceptionName
        );
        final String content = String.format(
                "Exception=[%s] Reason=[%s] topic=[%s] partition=[%d] offset=[%d] key=[%s] message=[%s]",
                exceptionName,
                exceptionMessage,
                topic,
                partition,
                offset,
                key,
                message
        );

        // 복구 가능한 예외 경우
        if(isRetryableException(exception)){
            log.info("복구 가능한 예외이지만 재시도 모두 실패되었으므로 DB 저장 후 배치를 통해 재처리 시도한다.");

            // 재시도는 이미 앞단(ErrorHandler 설정)에서 진행됨
            // DB 저장
        }

        // 복구 불가한 예외 경우
        else{
            // 로깅
            log.info("복구 불가한 예외이므로 개발자들에게 알림 발송한다.");

            // 개발자 알림 + DB 저장
            notificationUseCase.doNoti(
                    new NotificationRequestEventDto(
                            0L,
                            "sunghyun7895@naver.com",
                            subject,
                            content
                    )
            );
        }
    }

    private Throwable getRootCause(Exception exception){
        return exception != null && exception.getCause() != null ? exception.getCause() : exception;
    }

    private boolean isRetryableException(Exception exception) {
        if (exception == null) {
            return false;
        }

        // 1. 스프링 카프카가 감싸놓은 껍질(래퍼 예외)이 있다면 진짜 원인(Root Cause)을 꺼냅니다.
        Throwable rootCause = getRootCause(exception);

        // 2. 성현님이 지정한 '복구 불가능한 비즈니스 예외(BaseException)' 계열인지 확인합니다.
        // 3. (선택사항) 자바 표준 예외 중 명백히 개발자 버그인 경우도 복구 불가로 필터링하고 싶다면 추가 가능
        if (
                rootCause instanceof BaseException ||
                rootCause instanceof NullPointerException ||
                rootCause instanceof IllegalArgumentException
        )
        {
            // 복구 불가능하므로 false 반환
            return false;
        }

        // 4. 위의 '복구 불가 조건'들을 모두 피해갔다면 일시적 오류(외부 API 타임아웃, DB 락 등)로 간주하여 true 반환
        return true;
    }
}
