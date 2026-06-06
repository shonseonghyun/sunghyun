package com.sunghyun.plab.subscription.application;

import com.sunghyun.plab.subscription.application.port.in.MatchSubscriptionUseCase;
import com.sunghyun.plab.subscription.application.port.in.dto.MatchSubscriptionModReqDto;
import com.sunghyun.plab.subscription.application.port.in.dto.MatchSubscriptionRegReqDto;
import com.sunghyun.plab.subscription.application.port.out.dto.MatchSubscriptionModResDto;
import com.sunghyun.plab.subscription.application.port.out.dto.MatchSubscriptionRegResDto;
import com.sunghyun.plab.subscription.application.port.out.dto.NotificationRequestedEvent;
import com.sunghyun.plab.subscription.application.port.out.dto.PlabMatchResDto;
import com.sunghyun.plab.subscription.application.port.out.external.NotificationEventOutPort;
import com.sunghyun.plab.subscription.application.port.out.external.PlabMatchOutPort;
import com.sunghyun.plab.subscription.application.port.out.persistence.MatchSubscriptionRepository;
import com.sunghyun.plab.subscription.domain.enums.PlabNotiMessage;
import com.sunghyun.plab.subscription.domain.exception.ExistMatchSubscriptionException;
import com.sunghyun.plab.subscription.domain.exception.NotExistMatchSubscriptionException;
import com.sunghyun.plab.subscription.domain.model.MatchSubscription;
import com.sunghyun.plab.subscription.domain.service.MatchSubscriptionDomainService;
import com.sunghyun.plab.subscription.domain.service.SubscriptionNotificationValidator;
import com.sunghyun.utils.ApiUtils;
import com.sunghyun.web.ErrorCode;
import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Slf4j
@Service
@RequiredArgsConstructor
public class MatchSubscriptionService implements MatchSubscriptionUseCase {
    private final SubscriptionNotificationValidator subscriptionNotificationValidator;
    private final MatchSubscriptionDomainService matchSubscriptionDomainService;
    private final MatchSubscriptionRepository matchSubscriptionRepository;
    private final PlabMatchOutPort plabMatchOutPort;
    private final HikariDataSource hikariDataSource;

//    private final ApplicationEventPublisher eventPublisher;
//    private final KafkaTemplate<String,Object> kafkaTemplate; //카프카가 아니라 새로운 걸로 변환해야한다면?
    private final NotificationEventOutPort notificationEventOutPort;

    @Transactional
    public MatchSubscriptionRegResDto registerMatchSubscription(final MatchSubscriptionRegReqDto dto){

        int activeConnections = hikariDataSource.getHikariPoolMXBean().getActiveConnections();
        int idleConnections = hikariDataSource.getHikariPoolMXBean().getIdleConnections();
        System.out.println("registerMatchSubscription...");
        System.out.println("activeConnections = " + activeConnections);
        System.out.println("idleConnections = " + idleConnections);
        System.out.println("registerMatchSubscription...");

        // 플랩 매치데이터 조회
        final PlabMatchResDto result = plabMatchOutPort.registerPlabMatch(dto.getPlabMatchNo());

        // 매치 구독 존재하는지 검증
        matchSubscriptionRepository.findMatchSubscriptionByMemberNoAndPlabMatchNoAndNotiType(dto.getMemberNo(), dto.getPlabMatchNo(), dto.getNotiType())
                .ifPresent(m -> {
                    throw new ExistMatchSubscriptionException(ErrorCode.P01);
                })
        ;

        // 매치구독 도메인 생성
        MatchSubscription matchSubscription = matchSubscriptionDomainService.createMatchSubscription(
                dto.getPlabMatchNo(),
                dto.getMemberNo(),
                dto.getEmail(),
                dto.getNotiType(),
                dto.getValue()
        );

        // 매치 구독 저장
        MatchSubscription savedMatchSubscription = matchSubscriptionRepository.save(matchSubscription);

        // 조건에 부합하는 경우 카프카 이벤트 발행
        // 트랜잭션이 여기까지 묶여있어서 브로커 서버 종료되어 연결 2번의 재요청까지 모두 기다리게 되어 클라이언트에게 응답도 늦게 가고, kafka의 latency가 메인 비즈니스 로직에 전파된다.
        publishNotificationIfSatisfied(savedMatchSubscription,result);

        // 응답
        return MatchSubscriptionRegResDto.from(savedMatchSubscription,result);
    }

    @Transactional
    public MatchSubscriptionModResDto modifyMatchSubscription(final Long subscriptionNo, final MatchSubscriptionModReqDto dto){
        MatchSubscription selectedMatchSubscription = matchSubscriptionRepository.getMatchSubscriptionBySubscriptionNo(subscriptionNo)
                .orElseThrow(()->new NotExistMatchSubscriptionException(ErrorCode.P03))
                ;
        final MatchSubscription modifyReqMatchSubscription = dto.toDomain(selectedMatchSubscription.getNotiType());
        
        final PlabMatchResDto result = plabMatchOutPort.getPlabMatchByPlabMatchNo(selectedMatchSubscription.getPlabMatchNo());

        //업데이트 여부 플래그
        boolean isUpdated = ApiUtils.merge(modifyReqMatchSubscription, selectedMatchSubscription);
        if(isUpdated){
            //새로 변경했으모로 새롭게 알림 받을 수 있드록 false 수정
            matchSubscriptionRepository.save(selectedMatchSubscription);
        }

        publishNotificationIfSatisfied(selectedMatchSubscription,result);

        return MatchSubscriptionModResDto.from(selectedMatchSubscription);
    }

    private void publishNotificationIfSatisfied(final MatchSubscription matchSubscription,final PlabMatchResDto dto){
        String currentTransactionName = TransactionSynchronizationManager.getCurrentTransactionName();
        log.info("현재 활성화된 트랜잭션 이름: [{}]", currentTransactionName);

        final boolean isSatisfied = subscriptionNotificationValidator.isSatisfied(
                matchSubscription.getNotiType(),
                matchSubscription.getNotiValue(),
                dto.getPlayerCnt(),
                dto.getSubType()
        );

        if(!isSatisfied) return ;

        PlabNotiMessage strategy = PlabNotiMessage.valueOf(matchSubscription.getNotiType().name());

        notificationEventOutPort.publish(
                new NotificationRequestedEvent(
                        matchSubscription.getMemberNo(),
                        matchSubscription.getEmail(),
                        strategy.getSubject(dto),
                        strategy.getContent(dto)
                )
        );
    }
}
