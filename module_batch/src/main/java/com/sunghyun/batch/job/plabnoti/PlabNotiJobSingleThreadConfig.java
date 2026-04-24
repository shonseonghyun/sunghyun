package com.sunghyun.batch.job.plabnoti;

import com.sunghyun.batch.dto.*;
import com.sunghyun.batch.infrastructure.mapper.PlabNotiMapper;
import com.sunghyun.batch.job.plabnoti.step1.listener.MatchSyncWriterListener;
import com.sunghyun.batch.job.plabnoti.step1.PlabMatchSyncStepConfig;
import com.sunghyun.batch.job.plabnoti.step2.PlabMatchNotificationStepConfig;
import com.sunghyun.utils.ApiUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.batch.MyBatisCursorItemReader;
import org.mybatis.spring.batch.builder.MyBatisBatchItemWriterBuilder;
import org.mybatis.spring.batch.builder.MyBatisCursorItemReaderBuilder;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
