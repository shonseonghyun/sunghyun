package com.sunghyun.batch.job.plabnoti.step1.listener;

import com.sunghyun.batch.dto.MatchUpdateEvent;
import com.sunghyun.batch.dto.PlabMatchDtoWithFlg;
import com.sunghyun.plab.match.domain.enums.MatchStatus;
import com.sunghyun.plab.subscription.domain.enums.NotiType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.Chunk;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@StepScope //Step실행 시점에 빈 생성 / 이게 없으면 updatedMatchList 초기화 되지 않고, 싱글톤으로 해당 클래스가 생성되어 job돌 때 초기화되지 않고 사용되어지게 된다.
public class MatchSyncWriteListener implements ItemWriteListener<PlabMatchDtoWithFlg>, StepExecutionListener {
    private final List<MatchUpdateEvent> updatedMatchList = new ArrayList<>();

    @Override
    public void beforeStep(StepExecution stepExecution) {
        log.info(">>> [{}] Step 시작 시간: {}",
                stepExecution.getStepName(), stepExecution.getStartTime());
    }

    @Override
    public void afterWrite(Chunk<? extends PlabMatchDtoWithFlg> items) {
        log.info("WriterListener afterWriter 실행");

        for(PlabMatchDtoWithFlg item:items){

            // [추가] 404로 인해 무효화된 데이터라면 알림 대상(updatedMatchList)에서 제외합니다.
            if(item.getStatus().equals(MatchStatus.CANCELED)){
                log.info(">>> 매치번호 {} 는 무효화된 대상이므로 알림 대상에서 제외합니다.", item.getPlabMatchNo());
                continue;
            }

            if(item.isPlayerCntChanged()){
                final NotiType notiType = NotiType.PLAYER_COUNT;
                updatedMatchList.add(
                        new MatchUpdateEvent(
                                item.getPlabMatchNo(),
                                notiType,
                                item.getPlayerCnt()
                        )
                );
            }

            if(item.isSubTypeChanged()){
                final NotiType notiType = NotiType.FREE_SUB;
                updatedMatchList.add(
                        new MatchUpdateEvent(
                                item.getPlabMatchNo(),
                                notiType,
                                item.getSubType()
                        )
                );
            }
        }
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution){
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

        if(updatedMatchList.isEmpty()){
            log.info(">>> 업데이트된 매치가 없음. 다음 STEP을 건너뜁니다.");
            return new ExitStatus("NO_DATA");
        }

        log.info(">>> Step 1 성공: 업데이트된 매치 번호 {}개 공유함", updatedMatchList.size());
        stepExecution.getJobExecution().getExecutionContext()
                .put("updatedMatchList", updatedMatchList);
        return ExitStatus.COMPLETED;
    }
}
