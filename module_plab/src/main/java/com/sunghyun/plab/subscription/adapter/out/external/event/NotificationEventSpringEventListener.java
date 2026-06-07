package com.sunghyun.plab.subscription.adapter.out.external.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sunghyun.plab.subscription.application.OutBoxCommandService;
import com.sunghyun.plab.subscription.application.port.out.dto.NotificationRequestedEvent;
import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationEventSpringEventListener {
    private final KafkaTemplate<String,Object> kafkaTemplate;
    private final OutBoxCommandService outBoxCommandService;

//    private final ThreadPoolTaskExecutor taskExecutor;
    private final ObjectMapper om;
    private final HikariDataSource hikariDataSource;

    @Value("${plab.kafka.topics.subscription-noti}")
    private String topic;

//    private void logThreadPoolStatus(String location) {
//        int activeCount = taskExecutor.getActiveCount();    // 현재 일하고 있는 스레드 수
//        int poolSize = taskExecutor.getPoolSize();          // 현재 생성된 총 스레드 수
//        int queueSize = taskExecutor.getThreadPoolExecutor().getQueue().size(); // 큐에서 대기 중인 작업 수
//
//        log.info("[{}] 📊 스레드풀 상태 -> Active: {}, PoolSize: {}, Queue: {}",
//                location, activeCount, poolSize, queueSize);
//    }


    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void record(final NotificationRequestedEvent notificationRequestedEvent){
        log.info("BeforeCommit 아웃박스 테이블 저장 start");
        String currentTransactionName = TransactionSynchronizationManager.getCurrentTransactionName();
        log.info("현재 활성화된 트랜잭션 이름: [{}]", currentTransactionName);

        String jsonPayLoad = "";

        try{
            jsonPayLoad = om.writeValueAsString(notificationRequestedEvent);
        }catch (JsonProcessingException e){
            log.error("Json 직렬화 도중 실패하였습니다. 요청클래스=[{}]",notificationRequestedEvent.toString());
            //다른 애플리케이션 예외로 치환해 던지기
//            throw new
        }

        outBoxCommandService.save(notificationRequestedEvent.getOutBoxNo(),topic,jsonPayLoad);
    }


    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void sendEventV2(final NotificationRequestedEvent notificationRequestedEvent){
        log.info("AfterCommit 후 카프카 이벤트 발행v2 start. ID=[{}]", notificationRequestedEvent.getOutBoxNo());

        CompletableFuture<SendResult<String,Object>> future =  kafkaTemplate.send(topic,notificationRequestedEvent);

        // 비동기 콜백 등록
        // 단, 메타 데이터 조회 시 발생한 에러는 잡히지 않기에 catch 구문으로 잡거나 ProducerListener를 통해 후처리해야 한다.
        future.whenComplete((result,ex)->{
            if(ex==null){
                // 성공
                outBoxCommandService.updateStatus(notificationRequestedEvent.getOutBoxNo(), true);
            } else{
                log.error("카프카 이벤트 발행 중 예외가 발생하여 아웃박스를 FAILED로 변경합니다. [{}]", notificationRequestedEvent.getOutBoxNo(), ex);
                outBoxCommandService.updateStatus(notificationRequestedEvent.getOutBoxNo(),false);
            }
        });

        log.info("아웃박스 테이블 상태 변경 최종 반영 완료");
    }
}
