package com.sunghyun.plab.match.application.service;

import com.sunghyun.feign.dto.PlabMatchResponseDto;
import com.sunghyun.plab.match.application.port.in.PlabMatchUseCase;
import com.sunghyun.plab.match.application.port.out.feign.PlabOpenFeignClient;
import com.sunghyun.plab.match.application.port.out.repository.PlabMatchRepository;
import com.sunghyun.plab.match.domain.model.PlabMatch;
import com.sunghyun.plab.subscription.application.port.out.dto.PlabMatchResDto;
import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class PlabMatchService implements PlabMatchUseCase {
    private final PlabMatchRepository plabMatchRepository;
    private final PlabOpenFeignClient plabOpenFeignClient;

    private final HikariDataSource hikariDataSource;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    // 해당 메소드 종료되는 순간 커밋까지 완료되어야 한다.
    // PlabMatchRegisterFacade메소드의 트랜잭션이 현재는 없지만 미래 변경(부모 메소드의 트랜잭션 할당)으로부터의 방어 코딩을 위해 Requires_new로 명시해두는 편이 좋다.
    //concurrency 여러 유저가 동시에 동일한 매치를 등록하려고 할 때, 동시성은 어떻게 잡을 것 인가? -> 동시성은 redis 분산락으로 잡음
    public PlabMatchResDto registerPlabMatch(final Long plabMatchNo){
        int activeConnections = hikariDataSource.getHikariPoolMXBean().getActiveConnections();
        int idleConnections = hikariDataSource.getHikariPoolMXBean().getIdleConnections();
        System.out.println("registerPlabMatch...");
        System.out.println("activeConnections = " + activeConnections);
        System.out.println("idleConnections = " + idleConnections);

        String currentTransactionName = TransactionSynchronizationManager.getCurrentTransactionName();
        log.info("현재 활성화된 트랜잭션 이름: [{}]", currentTransactionName);

        return plabMatchRepository.getPlabMatchByPlabMatchNo(plabMatchNo)
                .map(plabMatch -> {
                    plabMatch.validateActiveStatus(); //도메인 계층으로 위임
                    return PlabMatchResDto.from(plabMatch);
                })
                .orElseGet(() -> {
                    //Plab Api 검증 처리
                    PlabMatchResponseDto result = plabOpenFeignClient.getMatch(plabMatchNo);

                    //PlabMatch 도메인 생성
                    PlabMatch plabMatch = PlabMatch.create(plabMatchNo,result);

                    //도메인 영속화
                    PlabMatch savedPlabMatch = plabMatchRepository.save(plabMatch);

                    //ResDto 변환
                    return PlabMatchResDto.from(savedPlabMatch);
                });
//        Optional<PlabMatch> selectedPlabMatch = plabMatchRepository.getPlabMatchByPlabMatchNo(plabMatchNo);
//        if(selectedPlabMatch.isEmpty()){
//            PlabMatch plabMatch = PlabMatch.create(plabOpenFeignClient,plabMatchNo);
//            return PlabMatchResDto.from(plabMatchRepository.save(plabMatch));
//        }
//        else{
//            return PlabMatchResDto.from(selectedPlabMatch.get());

//        plabMatchRepository.getPlabMatchByPlabMatchNo(plabMatchNo)
//                .orElseGet(()->{
//                    PlabMatch plabMatch = PlabMatch.create(plabOpenFeignClient,plabMatchNo);
//                    return plabMatchRepository.save(plabMatch);
//                });
    }

    @Transactional(readOnly = true)
    public PlabMatchResDto getPlabMatchByPlabMatchNo(final Long plabMatchNo) {
//        PlabMatch selectedPlabMatch = plabMatchRepository.getPlabMatchByPlabMatchNo(plabMatchNo)
//                .orElse(null);
//        return PlabMatchResDto.from(selectedPlabMatch);
        return plabMatchRepository.getPlabMatchByPlabMatchNo(plabMatchNo)
                .map(PlabMatchResDto::from)
                .orElse(null); // 혹은 필요시 예외 처리
    }

    @Override
    public List<PlabMatchResDto> getPlabMatches(final String startDt,final String endDt) {
        return plabMatchRepository.getPlabMatches(startDt,endDt)
                .stream().map(PlabMatchResDto::from)
                .toList()
        ;
    }
}
