package com.sunghyun.batch.job.plabnoti.step2.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;

@Slf4j
@Component
public class TimeCheckListener implements StepExecutionListener {
    @Override
    public void beforeStep(StepExecution stepExecution) {
        log.info(">>> [{}] Step 시작 시간: {}",
                stepExecution.getStepName(), stepExecution.getStartTime());
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        long duration = Duration.between(
                stepExecution.getStartTime(),
                LocalDateTime.now()
        ).toMillis();

        log.info(">>> [{}] Step 종료. 소요 시간: {}ms (읽기: {}건, 쓰기: {}건)",
                stepExecution.getStepName(),
                duration,
                stepExecution.getReadCount(),
                stepExecution.getWriteCount()
        );
        return stepExecution.getExitStatus();
    }
}
