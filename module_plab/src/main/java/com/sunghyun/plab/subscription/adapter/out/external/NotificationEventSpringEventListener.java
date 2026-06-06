package com.sunghyun.plab.subscription.adapter.out.external;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sunghyun.plab.subscription.application.OutBoxCommandService;
import com.sunghyun.plab.subscription.application.port.out.dto.NotificationRequestedEvent;
import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationEventSpringEventListener {
    private final KafkaTemplate<String,Object> kafkaTemplate;
    private final OutBoxCommandService outBoxCommandService;

    private final ObjectMapper om;
    private final HikariDataSource hikariDataSource;

    @Value("${plab.kafka.topics.subscription-noti}")
    private String topic;

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
    //default, requries_new, 트랜잭션X 중 뭐가 맞을까?
//    @Transactional //이건 어차피 안된다..
//    @Transactional(propagation = Propagation.REQUIRES_NEW) //이게 있으면 메인스레드에서 DB 커넥션을 반납하지 않고, 비동기 스레드 진입..
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void sendEvent(final NotificationRequestedEvent notificationRequestedEvent){
        log.info("AfterCommit 후 카프카 이벤트 발행 start. ID=[{}]", notificationRequestedEvent.getOutBoxNo());
        String currentTransactionName = TransactionSynchronizationManager.getCurrentTransactionName();
        log.info("현재 활성화된 트랜잭션 이름: [{}]", currentTransactionName);

        int activeConnections = hikariDataSource.getHikariPoolMXBean().getActiveConnections();
        int idleConnections = hikariDataSource.getHikariPoolMXBean().getIdleConnections();
        System.out.println("sendEvent...");
        System.out.println("activeConnections = " + activeConnections);
        System.out.println("idleConnections = " + idleConnections);

        boolean isPublished = false;

        try{
            kafkaTemplate.send(topic, notificationRequestedEvent).get();
            isPublished = true;
        }catch (Exception e){
            log.error("카프카 이벤트 발행 중 예외가 발생하여 아웃박스를 FAILED로 변경합니다. ID=[{}]", notificationRequestedEvent.getOutBoxNo(), e);
        }

        outBoxCommandService.updateStatus(notificationRequestedEvent.getOutBoxNo(), isPublished);
        log.info("아웃박스 테이블 상태 변경 최종 반영 완료");
    }
}
