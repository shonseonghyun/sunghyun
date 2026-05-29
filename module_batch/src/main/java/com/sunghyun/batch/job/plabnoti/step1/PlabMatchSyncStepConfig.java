package com.sunghyun.batch.job.plabnoti.step1;

import com.sunghyun.batch.dto.PlabMatchDto;
import com.sunghyun.batch.dto.PlabMatchDtoWithFlg;
import com.sunghyun.batch.job.plabnoti.step1.listener.MatchSyncWriteListener;
import com.sunghyun.feign.PlabExternalOpenFeignClient;
import com.sunghyun.feign.dto.PlabMatchResponseDto;
import com.sunghyun.plab.match.domain.enums.MatchStatus;
import com.sunghyun.plab.subscription.domain.enums.NotiSetting;
import com.sunghyun.web.exception.ExternalResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.batch.builder.MyBatisBatchItemWriterBuilder;
import org.mybatis.spring.batch.builder.MyBatisCursorItemReaderBuilder;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class PlabMatchSyncStepConfig {
    private final static int chunkSize = 10;

    private final PlabExternalOpenFeignClient plabExternalOpenFeignClient;
    private final SqlSessionFactory sqlSessionFactory;
    private final MatchSyncWriteListener matchSyncWriteListener;

    @Bean
    public Step plabMatchSyncStep(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager){
        return new StepBuilder("plabMatchSyncStep",jobRepository)
                .<PlabMatchDto, PlabMatchDtoWithFlg>chunk(chunkSize,platformTransactionManager)
                .reader(plabMatchSyncReader())
                .processor(plabMatchSyncProcessor())
                .writer(plabMatchSyncWriter())
                .listener((StepExecutionListener) matchSyncWriteListener)
                .listener((ItemWriteListener)matchSyncWriteListener)
                .build()
                ;
    }

    @Bean
    public ItemReader<PlabMatchDto> plabMatchSyncReader(){
        return new MyBatisCursorItemReaderBuilder<PlabMatchDto>()
                .sqlSessionFactory(sqlSessionFactory)
                .queryId("com.sunghyun.batch.infrastructure.mapper.PlabNotiMapper.getPlabMatches")
                .build()
                ;
    }

    @Bean
    public ItemProcessor<PlabMatchDto, PlabMatchDtoWithFlg> plabMatchSyncProcessor(){
        return plabMatch->{
            final Long plabMatchNo = plabMatch.getPlabMatchNo();
            log.info(">>> 매치번호 {} 상태 동기화 시작", plabMatchNo);

            //여기서 예외 발생할 수 있는데 어떻게 처리할꺼야?
            try{
                PlabMatchResponseDto result = plabExternalOpenFeignClient.getMatch(plabMatchNo);

                final NotiSetting totalApplyCnt = NotiSetting.fromCode(String.valueOf(result.getTotalApplyCnt()));
                final boolean isManagerFree = result.isManagerFree();
                final boolean isSuperSub = result.isSuperSub();
                final NotiSetting subType = NotiSetting.getSubType(isSuperSub,isManagerFree);


                // 2. 변경 여부 체크( 인원수, 서브타입 )
                boolean isPlayerCntChanged = !plabMatch.getPlayerCnt().equals(totalApplyCnt);
                boolean isSubTypeChanged = plabMatch.getSubType() != subType;

                if(isPlayerCntChanged){
                    log.info(">>> 매치번호 {} 데이터 변경 감지 (인원: {}->{})",
                            plabMatchNo,
                            plabMatch.getPlayerCnt(),
                            totalApplyCnt
                    );

                    plabMatch.setPlayerCnt(totalApplyCnt);
                }

                if(isSubTypeChanged) {
                    log.info(">>> 매치번호 {} 데이터 변경 감지 (타입: {}->{})",
                            plabMatchNo,
                            plabMatch.getSubType(),
                            subType
                    );

                    // DTO에 새로운 값 세팅
                    plabMatch.setSubType(subType);
                }

                if(isSubTypeChanged || isPlayerCntChanged) {
                    return PlabMatchDtoWithFlg.builder()
                            .matchNo(plabMatch.getMatchNo())
                            .plabMatchNo(plabMatch.getPlabMatchNo())
                            .stadiumName(plabMatch.getStadiumName())
                            .stadiumNo(plabMatch.getStadiumNo())
                            .matchTm(plabMatch.getMatchTm())
                            .matchDt(plabMatch.getMatchDt())
                            .playerCnt(totalApplyCnt)
                            .subType(subType)
                            .playerCntChanged(isPlayerCntChanged)
                            .subTypeChanged(isSubTypeChanged)
                            .status(plabMatch.getStatus())
                            .build()
                            ;
                }

                // 3. 변경사항이 없으면 null 리턴 (해당 아이템은 writer로 넘어가지 않음)
                log.info(">>> 매치번호 {} 변경사항 없음 - 건너뜁니다.", plabMatchNo);
                return null;
            }catch (ExternalResourceNotFoundException e){
                log.warn(">>> 해당 매치는 취소되었거나 api 내부 에러 발생한 것으로 보입니다. 무효화 대상으로 분리합니다.");
                return PlabMatchDtoWithFlg.builder()
                        .matchNo(plabMatch.getMatchNo())
                        .plabMatchNo(plabMatch.getPlabMatchNo())
                        .status(MatchStatus.CANCELED)
                        .build()
                        ;
            }
        };
    }

    @Bean
    public ItemWriter<PlabMatchDtoWithFlg> plabMatchSyncWriter(){

        //1. 정상 업데이트용 writer
        ItemWriter<PlabMatchDtoWithFlg> syncWriter = new MyBatisBatchItemWriterBuilder<PlabMatchDtoWithFlg>()
                .sqlSessionFactory(sqlSessionFactory)
                .statementId("com.sunghyun.batch.infrastructure.mapper.PlabNotiMapper.sync")
                .build();

        // 2. 404  발생 시 상태 변경용 writer
        ItemWriter<PlabMatchDtoWithFlg> invalidWriter = new MyBatisBatchItemWriterBuilder<PlabMatchDtoWithFlg>()
                .sqlSessionFactory(sqlSessionFactory)
                .statementId("com.sunghyun.batch.infrastructure.mapper.PlabNotiMapper.updateMatchStatusCanceled")
                .build();

        return items -> {
            for (PlabMatchDtoWithFlg item : items) {
                if (item.getStatus().equals(MatchStatus.CANCELED)) {
                    log.info("상태 변경 writer 실행");
                    invalidWriter.write(Chunk.of(item));
                } else {
                    syncWriter.write(Chunk.of(item));
                }
            }
        };
    }
}
