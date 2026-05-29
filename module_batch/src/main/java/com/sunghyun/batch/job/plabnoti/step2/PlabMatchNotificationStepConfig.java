package com.sunghyun.batch.job.plabnoti.step2;

import com.sunghyun.batch.dto.MatchUpdateEvent;
import com.sunghyun.batch.dto.NotiHistoryDto;
import com.sunghyun.batch.dto.NotificationTargetDto;
import com.sunghyun.batch.job.plabnoti.step2.listener.TimeCheckListener;
import com.sunghyun.notification.application.port.in.NotificationUseCase;
import com.sunghyun.plab.subscription.domain.enums.PlabNotiMessage;
import com.sunghyun.plab.subscription.domain.service.SubscriptionNotificationValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.batch.MyBatisCursorItemReader;
import org.mybatis.spring.batch.builder.MyBatisCursorItemReaderBuilder;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class PlabMatchNotificationStepConfig {
    private final static int chunkSize = 10;
    private final SqlSessionFactory sqlSessionFactory;
    private final NotificationUseCase notificationService;
    private final TimeCheckListener timeCheckListener;
    private final SubscriptionNotificationValidator subscriptionNotificationValidator;

    @Bean
    public Step plabMatchNotificationStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("plabMatchNotificationStep", jobRepository)
                .<NotificationTargetDto, NotiHistoryDto>chunk(chunkSize, transactionManager)
                .reader(plabMatchNotificationReader(null))
                .processor(plabMatchNotificationProcessor())
                .writer(plabMatchNotificationWriter())
                .listener(timeCheckListener)
                .build();
    }


    @Bean
    @StepScope
    public MyBatisCursorItemReader<NotificationTargetDto> plabMatchNotificationReader(
            @Value("#{jobExecutionContext['updatedMatchList']}") List<MatchUpdateEvent> updatedMatchList){
        log.info(">>> Step 2 읽기 시작: 공유받은 매치 번호들 = {}", updatedMatchList);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("updatedMatchList", updatedMatchList);

        return new MyBatisCursorItemReaderBuilder<NotificationTargetDto>()
                .sqlSessionFactory(sqlSessionFactory)
                .queryId("com.sunghyun.batch.infrastructure.mapper.PlabNotiMapper.findNotificationTargets")
                .parameterValues(parameters)
                .build()
                ;
    }

    @Bean
    @StepScope
    public ItemProcessor<NotificationTargetDto,NotiHistoryDto> plabMatchNotificationProcessor(){
        return item -> {
            log.info(">>> 알림 조건 검증 시작: 구독번호={}, 매치번호={}, 노티 타입={}, 값={}",
                    item.getSubscriptionNo(), item.getPlabMatchNo(), item.getNotiType(), item.getNotiValue()
            );

            // 2. 통합 비교 (타입이 무엇이든 유저가 설정한 값과 현재 값이 같으면 발송)
            if (subscriptionNotificationValidator.isSatisfied(item.getNotiType(), item.getNotiValue(),item.getPlayerCnt(),item.getSubType())) {
                log.info("[성공] 알림 대상입니다.");

                return NotiHistoryDto.builder()
                        .subscriptionNo(item.getSubscriptionNo())
                        .memberNo(item.getMemberNo())
                        .email(item.getEmail())
                        .notificationTargetDto(item)
                        .build()
                        ;
            }

            return null; // 조건 안 맞으면 Writer로 안 넘어감
        };
    }

    @Bean
    public ItemWriter<NotiHistoryDto> plabMatchNotificationWriter(){
        return items->{
            log.info(">>> [Writer] {}건의 알림 발송 및 이력 저장 시작", items.size());

            for(NotiHistoryDto item:items){
                // 1. 배치의 DTO에서 알림 서비스에 필요한 데이터 추출
                final Long memberNo = item.getMemberNo();
                final String email = item.getEmail();
                NotificationTargetDto target = item.getNotificationTargetDto();

                // 2. 전략(메시지 포맷) 결정
                PlabNotiMessage strategy = PlabNotiMessage.valueOf(target.getNotiType().name());

                // 3. 알림 서비스 호출(발송+ 이력 저장 한번에 일어남)
                notificationService.doNoti(memberNo,email,strategy,target);
            }

            log.info(">>> [Writer] Chunk 처리 완료");
        };
    }

//    @Bean
//    public ItemWriter<NotiHistoryDto> plabMatchNotificationWriter(){
//        PlabNotiMessage strategy = PlabNotiMessage.valueOf(item.getNotiType().name());
//        mailService.send(item.getEmail(),strategy,item);
//
//        return new MyBatisBatchItemWriterBuilder<NotiHistoryDto>()
//                .sqlSessionFactory(sqlSessionFactory)
//                .statementId("com.sunghyun.batch.infrastructure.mapper.PlabNotiMapper.insertNotiHistory")
//                .assertUpdates(false) // 업데이트된 로우가 없어도 에러 내지 않음
//                .build();
//    }
}
