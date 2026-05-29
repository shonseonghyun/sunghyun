package com.sunghyun.plab.subscription.application;

import com.sunghyun.notification.domain.event.NotificationEvent;
import com.sunghyun.plab.subscription.application.port.in.MatchSubscriptionUseCase;
import com.sunghyun.plab.subscription.application.port.in.dto.MatchSubscriptionModReqDto;
import com.sunghyun.plab.subscription.application.port.in.dto.MatchSubscriptionRegReqDto;
import com.sunghyun.plab.subscription.application.port.out.dto.MatchSubscriptionModResDto;
import com.sunghyun.plab.subscription.application.port.out.dto.MatchSubscriptionRegResDto;
import com.sunghyun.plab.subscription.application.port.out.dto.PlabMatchResDto;
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
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MatchSubscriptionService implements MatchSubscriptionUseCase {
    private final SubscriptionNotificationValidator subscriptionNotificationValidator;
    private final MatchSubscriptionDomainService matchSubscriptionDomainService;
    private final MatchSubscriptionRepository matchSubscriptionRepository;
    private final PlabMatchOutPort plabMatchOutPort;
    private final ApplicationEventPublisher eventPublisher;

    private final HikariDataSource hikariDataSource;


    @Transactional
    public MatchSubscriptionRegResDto registerMatchSubscription(final MatchSubscriptionRegReqDto dto){
        int activeConnections = hikariDataSource.getHikariPoolMXBean().getActiveConnections();
        int idleConnections = hikariDataSource.getHikariPoolMXBean().getIdleConnections();
        System.out.println("registerMatchSubscription...");
        System.out.println("activeConnections = " + activeConnections);
        System.out.println("idleConnections = " + idleConnections);
        System.out.println("registerMatchSubscription...");

        /* 플랩 매치 등록 */
        //플랩 매치 등록
        // 근데 이게 굳이 동기로 이루어져야할까???
        //하나의 트랜잭션 안에서 두 애그리거트가 영속화되도 되나? ㄴㄴ
        //=>두 메소드느는 서로 트랜잭션이 분리되어있다. + 트랜잭션을 최대한 짧게 잡으려 함
        //만약 MSA 아키텍쳐 변경으로 인해 모듈 간 클래스 참조 자체가 불가능해진다면 어떻게 할 것인가? (plabMatchService,plabMatchResDto ..)
//        PlabMatchResDto result = plabMatchService.registerPlabMatch(dto.getPlabMatchNo());
        //현재 인터페이스로 두고, 추후 openfeign으로 해야할 시 구현체만 바꿔주면 된다.
        PlabMatchResDto result = plabMatchOutPort.registerPlabMatch(dto.getPlabMatchNo());

        /* 구독 매치 등록 */
        //구독 매치 등록 검증
        matchSubscriptionRepository.findMatchSubscriptionByMemberNoAndPlabMatchNoAndNotiType(dto.getMemberNo(), dto.getPlabMatchNo(), dto.getNotiType())
                .ifPresent(m -> {
                    throw new ExistMatchSubscriptionException(ErrorCode.P01);
                })
        ;

        // 매치 구독 등록
        MatchSubscription matchSubscription = matchSubscriptionDomainService.createMatchSubscription(
                dto.getPlabMatchNo(),
                dto.getMemberNo(),
                dto.getEmail(),
                dto.getNotiType(),
                dto.getValue()
        );

        //도메인 서비스 내에서 영속화되는 게 아닌 애플리케이션 레이어에서 해야 하지 않나? 얼추 맞는 말이다. 대신, 트랜잭션 분리 여부를 확인해야 한다.
        MatchSubscription savedMatchSubscription = matchSubscriptionRepository.save(matchSubscription);

        if(subscriptionNotificationValidator.isSatisfied(
                savedMatchSubscription.getNotiType(),
                savedMatchSubscription.getNotiValue(),
                result.getPlayerCnt(),
                result.getSubType())
        ){
            // 메일 발송
            // 대신 비동기 + 해당 작업 성공 여부가 비즈니스 로직(매치 구독 저장)에 영향을 미쳐선 안 된다.
            PlabNotiMessage strategy = PlabNotiMessage.valueOf(savedMatchSubscription.getNotiType().name());
            eventPublisher.publishEvent(
                    new NotificationEvent<>(
                            matchSubscription.getMemberNo(),
                            matchSubscription.getEmail(),
                            strategy,
                            result
                    )
            );
        }

        log.info("MatchSubscriptionService register 응답");

        return MatchSubscriptionRegResDto.from(savedMatchSubscription,result);
    }

    @Transactional
    public MatchSubscriptionModResDto modifyMatchSubscription(final Long subscriptionNo, final MatchSubscriptionModReqDto dto){
        MatchSubscription selectedMatchSubscription = matchSubscriptionRepository.getMatchSubscriptionBySubscriptionNo(subscriptionNo)
                .orElseThrow(()->new NotExistMatchSubscriptionException(ErrorCode.P03))
                ;
        PlabMatchResDto result = plabMatchOutPort.getPlabMatchByPlabMatchNo(selectedMatchSubscription.getPlabMatchNo());
        final MatchSubscription modifyReqMatchSubscription = dto.toDomain(selectedMatchSubscription.getNotiType());

        //업데이트 여부 플래그
        boolean isUpdated = ApiUtils.merge(modifyReqMatchSubscription, selectedMatchSubscription);
        if(isUpdated){
            //새로 변경했으모로 새롭게 알림 받을 수 있드록 false 수정
            matchSubscriptionRepository.save(selectedMatchSubscription);
        }

        if(subscriptionNotificationValidator.isSatisfied(
                selectedMatchSubscription.getNotiType(),
                selectedMatchSubscription.getNotiValue(),
                result.getPlayerCnt(),
                result.getSubType())
        ){
            // 메일 발송
            // 대신 비동기 + 해당 작업 성공 여부가 비즈니스 로직(매치 구독 저장)에 영향을 미쳐선 안 된다.
            PlabNotiMessage strategy = PlabNotiMessage.valueOf(selectedMatchSubscription.getNotiType().name());
            eventPublisher.publishEvent(
                    new NotificationEvent<>(
                            selectedMatchSubscription.getSubscriptionNo(),
                            selectedMatchSubscription.getEmail(),
                            strategy,
                            result
                    )
            );
        }

        return MatchSubscriptionModResDto.from(selectedMatchSubscription);
    }
}
