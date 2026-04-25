package com.sunghyun.batch.job.plabnoti.step2;

import com.sunghyun.batch.dto.MatchUpdateEvent;
import com.sunghyun.batch.dto.NotiHistoryDto;
import com.sunghyun.batch.dto.NotificationTargetDto;
import com.sunghyun.batch.job.plabnoti.step2.listener.TimeCheckListener;
import com.sunghyun.mail.MailService;
import com.sunghyun.utils.ApiUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.batch.MyBatisCursorItemReader;
import org.mybatis.spring.batch.builder.MyBatisBatchItemWriterBuilder;
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
    private final MailService mailService;
    private final TimeCheckListener timeCheckListener;

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
                    item.getSubscriptionNo(), item.getPlabMatchNo(),item.getNotiType(),item.getNotiValue()
            );

            final String notiType = item.getNotiType();
            final String notiValue = item.getNotiValue();
            final String currentValue = notiType.equals("PLAYER_COUNT")
                    ? item.getCurrentPlayerCnt()
                    : item.getCurrentSubType()
                    ;

            log.info("[검증] 구독#{} | 타입:{} | 목표:{} vs 현재:{}",
                    item.getSubscriptionNo(),notiType, notiValue, currentValue);

            // 2. 통합 비교 (타입이 무엇이든 유저가 설정한 값과 현재 값이 같으면 발송)
            if (notiValue.equals(currentValue)) {
                log.info("[성공] 조건 일치! 알림을 발송합니다.");

                PlabNotiMailMessage strategy = PlabNotiMailMessage.valueOf(notiType);
                mailService.send(item.getEmail(),strategy,item);

                return NotiHistoryDto.builder()
                        .subscriptionNo(item.getSubscriptionNo())
                        .memberNo(item.getMemberNo())
                        .email(item.getEmail())
                        .notiType(notiType)
                        .notiValue(notiValue)
                        .sendDt(ApiUtils.getCurrentDt())
                        .sendTm(ApiUtils.getCurrentTm())
                        .build()
                        ;
            }

            return null; // 조건 안 맞으면 Writer로 안 넘어감
        };
    }

    @Bean
    public ItemWriter<NotiHistoryDto> plabMatchNotificationWriter(){
        return new MyBatisBatchItemWriterBuilder<NotiHistoryDto>()
                .sqlSessionFactory(sqlSessionFactory)
                .statementId("com.sunghyun.batch.infrastructure.mapper.PlabNotiMapper.insertNotiHistory")
                .assertUpdates(false) // 업데이트된 로우가 없어도 에러 내지 않음
                .build();
    }
}
