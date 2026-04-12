package com.sunghyun.batch.job.plabnoti;

import com.sunghyun.batch.dto.ActiveSubType;
import com.sunghyun.batch.dto.NotificationTargetDto;
import com.sunghyun.batch.dto.PlabMatchDto;
import com.sunghyun.batch.infrastructure.mapper.PlabNotiMapper;
import com.sunghyun.batch.job.plabnoti.listener.Listener2;
import com.sunghyun.batch.job.plabnoti.listener.MatchSyncWriterListener;
import com.sunghyun.feign.PlabExternalOpenFeignClient;
import com.sunghyun.feign.dto.PlabMatchResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
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
import org.springframework.batch.item.ItemReader;
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
public class PlabNotiJobSingleThreadConfig {
    private final static int chunkSize = 10;
    private final PlabExternalOpenFeignClient plabExternalOpenFeignClient;
    private final PlabNotiMapper plabNotiMapper;
    private final SqlSessionFactory sqlSessionFactory;
    private final MatchSyncWriterListener matchSyncWriterListener;
    private final Listener2 listener2;

    @Bean
    public Job sendNotificationJob(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager){
        return new JobBuilder("sendNotificationJob",jobRepository)
                .start(step1_PlabMatchSync(jobRepository,platformTransactionManager))
                    .on("NO_DATA")              //상태가 NO_DATA라면
                    .end()                              //여기서 바로 성공적으로 종료(Step 2 실행 안함)
                .from(step1_PlabMatchSync(jobRepository,platformTransactionManager))
                        .on("COMPLETED")        //상태가 COMPLETED라면
                        .to(step2_Notification(jobRepository,platformTransactionManager)) //step2 실행
                .end()
                .build()
                ;
    }

    //2개의 step으로 구성한다.
    //step1 완료후 step2 도중 에러 발생해도 step2부터 재시작할 수 있기 때문이다. 만약 1개의 step으로만 이루어져있을 경우, 매치 동기화부터 다시 시작해야 하게 된다.
    @Bean
    public Step step1_PlabMatchSync(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager){
        return new StepBuilder("step1_PlabMatchSync",jobRepository)
                .<PlabMatchDto,PlabMatchDto>chunk(chunkSize,platformTransactionManager)
                .reader(reader1())
                .processor(processor1())
                .writer(writer1())
                .listener((StepExecutionListener) matchSyncWriterListener)
                .build()
                ;
    }

    @Bean
    public ItemReader<PlabMatchDto> reader1(){
        return new MyBatisCursorItemReaderBuilder<PlabMatchDto>()
                .sqlSessionFactory(sqlSessionFactory)
                .queryId("com.sunghyun.batch.infrastructure.mapper.PlabNotiMapper.getPlabMatches")
                .build()
                ;
    }

    @Bean
    public ItemProcessor<PlabMatchDto,PlabMatchDto> processor1(){
        return dto->{
            final Long plabMatchNo = dto.getPlabMatchNo();
            log.info(">>> 매치번호 {} 상태 동기화 시작", plabMatchNo);

            //여기서 예외 발생할 수 있는데 어떻게 처리할꺼야?
            PlabMatchResponseDto result = plabExternalOpenFeignClient.getMatch(plabMatchNo);

            final Integer totalApplyCnt = result.getTotalApplyCnt();
            final boolean isManagerFree = result.isManagerFree();
            final boolean isSuperSub = result.isSuperSub();
            final ActiveSubType plbSubType = ActiveSubType.getSubType(isSuperSub,isManagerFree);


            //dto의 currentPlayerCnt와 totalApplCnt 값이 다를 경우 또는 dto의 subType과 plabSubType값이 다를 경우
            // 2. 변경 여부 체크 (값 비교)
            // - 인원수가 다르거나
            // - 서브타입(Enum)이 다르거나
            // - (추가) 경기장 이름 등이 변경되었을 경우
            boolean isChanged = !dto.getCurrentPlayerCnt().equals(totalApplyCnt)
                    || dto.getSubType() != plbSubType;

            if (isChanged) {
                log.info(">>> 매치번호 {} 데이터 변경 감지 (인원: {}->{}, 타입: {}->{})",
                        plabMatchNo, dto.getCurrentPlayerCnt(), totalApplyCnt, dto.getSubType(), plbSubType);

                // DTO에 새로운 값 세팅
                dto.setCurrentPlayerCnt(totalApplyCnt);
                dto.setSubType(plbSubType);

                return dto; // 변경사항이 있으면 writer로 전달
            }

            // 3. 변경사항이 없으면 null 리턴 (해당 아이템은 writer로 넘어가지 않음)
            log.info(">>> 매치번호 {} 변경사항 없음 - 건너뜁니다.", plabMatchNo);
            return null;
        };
    }

    @Bean
    public ItemWriter<PlabMatchDto> writer1(){
        return new MyBatisBatchItemWriterBuilder<PlabMatchDto>()
                .sqlSessionFactory(sqlSessionFactory)
                .statementId("com.sunghyun.batch.infrastructure.mapper.PlabNotiMapper.sync")
                .build();
    }


    @Bean
    public Step step2_Notification(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager){
        return new StepBuilder("step2_Notification",jobRepository)
                .<NotificationTargetDto,NotificationTargetDto>chunk(chunkSize,platformTransactionManager)
                .reader(reader2(null))
                .processor(processor2())
                .writer(writer2())
                .listener(listener2)
                .build()
                ;
    }


    @Bean
    @StepScope
    public MyBatisCursorItemReader<NotificationTargetDto> reader2(
            @Value("#{jobExecutionContext['updatedMatchNoList']}") List<Long> updatedMatchNoList){
        log.info(">>> Step 2 읽기 시작: 공유받은 매치 번호들 = {}", updatedMatchNoList);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("matchNoList", updatedMatchNoList);

        return new MyBatisCursorItemReaderBuilder<NotificationTargetDto>()
                .sqlSessionFactory(sqlSessionFactory)
                .queryId("com.sunghyun.batch.infrastructure.mapper.PlabNotiMapper.find")
                .parameterValues(parameters)
                .build()
                ;
    }

    @Bean
    @StepScope
    public ItemProcessor<NotificationTargetDto,NotificationTargetDto> processor2(){
        return item -> {
            boolean notifyPlayerCntFlg = item.isEqualsPlayerCnt();
            boolean notifySubType = item.isEqualsSubType();

            if(notifyPlayerCntFlg){
                //전송
                log.info(">>> 알림 전송[{}] {} / {}",item.getEmail(),"[플랩]인원 충족 알림",item.getCurrentPlayerCnt()+"명의 인원이 충족");
            }

            if(notifySubType){
                //전송
                log.info(">>> 알림 전송[{}] {} / {}",item.getEmail(),"[플랩]"+item.getCurrentSubType().getDesc()+"알림",item.getCurrentSubType().getDesc());
            }

            return null;
        };
    }

    @Bean
    public ItemWriter<NotificationTargetDto> writer2(){
        return new MyBatisBatchItemWriterBuilder<NotificationTargetDto>()
                .sqlSessionFactory(sqlSessionFactory)
                .statementId("com.sunghyun.batch.infrastructure.mapper.PlabNotiMapper.update")
                .assertUpdates(false) // 업데이트된 로우가 없어도 에러 내지 않음
                .build();
    }

}
