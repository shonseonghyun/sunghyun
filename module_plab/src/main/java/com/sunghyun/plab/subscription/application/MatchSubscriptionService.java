package com.sunghyun.plab.subscription.application;

import com.sunghyun.plab.subscription.application.port.in.MatchSubscriptionUseCase;
import com.sunghyun.plab.subscription.application.port.in.dto.MatchSubscriptionModReqDto;
import com.sunghyun.plab.subscription.application.port.in.dto.MatchSubscriptionRegReqDto;
import com.sunghyun.plab.subscription.application.port.out.dto.*;
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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MatchSubscriptionService implements MatchSubscriptionUseCase {
    private final SubscriptionNotificationValidator subscriptionNotificationValidator;
    private final MatchSubscriptionDomainService matchSubscriptionDomainService;
    private final MatchSubscriptionRepository matchSubscriptionRepository;
    private final PlabMatchOutPort plabMatchOutPort;
    private final NotificationEventOutPort notificationEventOutPort;

    @Override
    public List<MatchSubscriptionsSelResDto> getMatchSubscriptionsByDate(final Long memberNo,final String targetDate) {
        List<PlabMatchResDto> plabMatchResDtos = plabMatchOutPort.getPlabMatchesByDate(targetDate);

        Map<Long,PlabMatchResDto> matchMap = plabMatchResDtos.stream()
                .collect(Collectors.toMap(PlabMatchResDto::getPlabMatchNo,plabMatchResDto->plabMatchResDto))
                ;

        List<Long> targetMatchNos = new ArrayList<>(matchMap.keySet());
        List<MatchSubscription> matchSubscriptions = matchSubscriptionRepository.getMatchSubscriptions(memberNo,targetMatchNos);

        return matchSubscriptions.stream()
                .filter(sub->matchMap.containsKey(sub.getPlabMatchNo()))
                .map(sub->{
                    PlabMatchResDto plabMatchResDto = matchMap.get(sub.getPlabMatchNo());
                    return MatchSubscriptionsSelResDto.builder()
                            .matchSubscription(
                                    MatchSubscriptionSelResDto.builder()
                                            .plabMatchNo(sub.getPlabMatchNo())
                                            .subscriptionNo(sub.getSubscriptionNo())
                                            .memberNo(sub.getMemberNo())
                                            .email(sub.getEmail())
                                            .notiType(sub.getNotiType())
                                            .notiValue(sub.getNotiValue())
                                            .build())
                            .plabMatch(plabMatchResDto)
                            .build();
                }).toList();
    }

    @Override
    public List<MatchSubscriptionsSelResDto> getMatchSubscriptions(final Long memberNo,final String startDt,final String endDt) {
        List<PlabMatchResDto> plabMatchResDtos = plabMatchOutPort.getPlabMatches(startDt,endDt);
        Map<Long,PlabMatchResDto> matchMap = plabMatchResDtos.stream()
                .collect(Collectors.toMap(PlabMatchResDto::getPlabMatchNo,plabMatchResDto->plabMatchResDto))
                ;

        List<Long> targetMatchNos = new ArrayList<>(matchMap.keySet());
        List<MatchSubscription> matchSubscriptions = matchSubscriptionRepository.getMatchSubscriptions(memberNo,targetMatchNos);

        return  matchSubscriptions.stream()
                    .filter(sub->matchMap.containsKey(sub.getPlabMatchNo()))
                    .map(sub->{
                        PlabMatchResDto plabMatchResDto = matchMap.get(sub.getPlabMatchNo());
                        return MatchSubscriptionsSelResDto.builder()
                                .matchSubscription(
                                        MatchSubscriptionSelResDto.builder()
                                        .plabMatchNo(sub.getPlabMatchNo())
                                        .subscriptionNo(sub.getSubscriptionNo())
                                        .memberNo(sub.getMemberNo())
                                        .email(sub.getEmail())
                                        .notiType(sub.getNotiType())
                                        .notiValue(sub.getNotiValue())
                                        .build())
                                .plabMatch(plabMatchResDto)
                                .build();
                    }).toList()
                ;

    }

    @Transactional
    public MatchSubscriptionRegResDto registerMatchSubscription(final MatchSubscriptionRegReqDto dto){
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
        
        final PlabMatchResDto result = plabMatchOutPort.getPlabMatch(selectedMatchSubscription.getPlabMatchNo());

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
