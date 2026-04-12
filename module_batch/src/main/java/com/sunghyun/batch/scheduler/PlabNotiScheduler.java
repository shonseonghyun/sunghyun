package com.sunghyun.batch.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class PlabNotiScheduler {
    private final JobLauncher jobLauncher;
    private final Job sendNotificationJob;

    // 1분마다 실행 (60000ms)
    @Scheduled(fixedDelay = 10000)
    public void runJob() {
        try {
            log.info("Batch Job 시작: {}", LocalDateTime.now());

            // 매번 새로운 파라미터를 줘야 동일한 Job이 다시 실행됩니다.
            JobParameters params = new JobParametersBuilder()
                    .addLong("time", System.currentTimeMillis())
                    .toJobParameters();

            jobLauncher.run(sendNotificationJob, params);

        } catch (Exception e) {
            log.error("Batch Job 실행 중 에러 발생: ", e);
        }
    }

}
