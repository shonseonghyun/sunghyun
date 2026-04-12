package com.sunghyun.batch.job.plabnoti.listener;

import com.sunghyun.batch.dto.PlabMatchDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.Chunk;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@StepScope //Step실행 시점에 빈 생성 / 이게 없으면 updatedMatchList초기화 되지 않고, 싱글톤으로 해당 클래스가 생성되어 job돌 때 초기화되지 않고 사용되어지게 된다.
public class MatchSyncWriterListener implements ItemWriteListener<PlabMatchDto>, StepExecutionListener {
    private final List<Long> updatedMatchList = new ArrayList<>();

    @Override
    public void afterWrite(Chunk<? extends PlabMatchDto> items) {
        log.info("WriterListener afterWriter 실행");
        for(PlabMatchDto dto:items){
            log.info(">>>> 플랩 매치번호: {}",dto.getPlabMatchNo());
            updatedMatchList.add(dto.getPlabMatchNo());
        }
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution){
        if(updatedMatchList.isEmpty()){
            log.info(">>> 업데이트된 매치가 없음. Step2를 건너뜁니다.");
            return new ExitStatus("NO_DATA");
        }

        log.info(">>> Step 1 성공: 업데이트된 매치 번호 {}개 공유함", updatedMatchList.size());
        stepExecution.getJobExecution().getExecutionContext()
                .put("updatedMatchNoList", updatedMatchList);
        return ExitStatus.COMPLETED;
    }
}
