package com.sunghyun.batch.job.plabnoti.step1;

import com.sunghyun.batch.dto.ActiveSubType;
import com.sunghyun.batch.dto.PlabMatchDto;
import com.sunghyun.batch.dto.PlabMatchDtoWithFlg;
import com.sunghyun.batch.job.plabnoti.step1.listener.MatchSyncWriteListener;
import com.sunghyun.feign.PlabExternalOpenFeignClient;
import com.sunghyun.feign.dto.PlabMatchResponseDto;
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
        return dto->{
            final Long plabMatchNo = dto.getPlabMatchNo();
            log.info(">>> 매치번호 {} 상태 동기화 시작", plabMatchNo);

            //여기서 예외 발생할 수 있는데 어떻게 처리할꺼야?
            PlabMatchResponseDto result = plabExternalOpenFeignClient.getMatch(plabMatchNo);

            final Integer totalApplyCnt = result.getTotalApplyCnt();
            final boolean isManagerFree = result.isManagerFree();
            final boolean isSuperSub = result.isSuperSub();
            final ActiveSubType plabSubType = ActiveSubType.getSubType(isSuperSub,isManagerFree);


            // 2. 변경 여부 체크 (값 비교)
            // - 인원수가 다르거나
            // - 서브타입(Enum)이 다르거나
            boolean isPlayerCntChanged = !dto.getCurrentPlayerCnt().equals(totalApplyCnt);
            boolean isSubTypeChanged = dto.getSubType() != plabSubType;

            if(isPlayerCntChanged){
                log.info(">>> 매치번호 {} 데이터 변경 감지 (인원: {}->{})",
                        plabMatchNo,
                        dto.getCurrentPlayerCnt(),
                        totalApplyCnt
                );

                dto.setCurrentPlayerCnt(totalApplyCnt);
            }

            if(isSubTypeChanged) {
                log.info(">>> 매치번호 {} 데이터 변경 감지 (타입: {}->{})",
                        plabMatchNo,
                        dto.getSubType(),
                        plabSubType
                );

                // DTO에 새로운 값 세팅
                dto.setSubType(plabSubType);
            }

            if(isSubTypeChanged || isPlayerCntChanged) {
                return PlabMatchDtoWithFlg.builder()
                        .matchNo(dto.getMatchNo())
                        .plabMatchNo(dto.getPlabMatchNo())
                        .stadiumName(dto.getStadiumName())
                        .stadiumNo(dto.getStadiumNo())
                        .matchTm(dto.getMatchTm())
                        .matchDt(dto.getMatchDt())
                        .currentPlayerCnt(totalApplyCnt)
                        .subType(plabSubType)
                        .playerCntChanged(isPlayerCntChanged)
                        .subTypeChanged(isSubTypeChanged)
                        .build()
                        ;
            }

            // 3. 변경사항이 없으면 null 리턴 (해당 아이템은 writer로 넘어가지 않음)
            log.info(">>> 매치번호 {} 변경사항 없음 - 건너뜁니다.", plabMatchNo);
            return null;
        };
    }

    @Bean
    public ItemWriter<PlabMatchDtoWithFlg> plabMatchSyncWriter(){
        return new MyBatisBatchItemWriterBuilder<PlabMatchDtoWithFlg>()
                .sqlSessionFactory(sqlSessionFactory)
                .statementId("com.sunghyun.batch.infrastructure.mapper.PlabNotiMapper.sync")
                .build();
    }
}
