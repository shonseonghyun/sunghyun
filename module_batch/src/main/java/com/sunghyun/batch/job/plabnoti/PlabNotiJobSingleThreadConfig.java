package com.sunghyun.batch.job.plabnoti;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class PlabNotiJobSingleThreadConfig {
    private final Step plabMatchSyncStep;
    private final Step plabMatchNotificationStep;


    @Bean
    public Job sendNotificationJob(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager){
        return new JobBuilder("sendNotificationJob",jobRepository)
                .start(plabMatchSyncStep)
                    .on("NO_DATA")              //상태가 NO_DATA라면
                    .end()                              //여기서 바로 성공적으로 종료(Step 2 실행 안함)
                .from(plabMatchSyncStep)
                        .on("COMPLETED")        //상태가 COMPLETED라면
                        .to(plabMatchNotificationStep) //step2 실행
                .end()
                .build()
                ;
    }
}
