package com.sunghyun.plab.subscription.application;

import com.sunghyun.plab.subscription.application.port.in.MatchSubscriptionUseCase;
import com.sunghyun.plab.subscription.application.port.in.dto.MatchSubscriptionModReqDto;
import com.sunghyun.plab.subscription.application.port.in.dto.MatchSubscriptionRegReqDto;
import com.sunghyun.plab.subscription.application.port.out.dto.MatchSubscriptionModResDto;
import com.sunghyun.plab.subscription.application.port.out.dto.MatchSubscriptionRegResDto;
import com.sunghyun.plab.subscription.application.port.out.dto.PlabMatchResDto;
import com.sunghyun.plab.subscription.application.port.out.external.PlabMatchOutPort;
import com.sunghyun.plab.subscription.application.port.out.persistence.MatchSubscriptionRepository;
import com.sunghyun.plab.subscription.domain.exception.ExistMatchSubscriptionException;
import com.sunghyun.plab.subscription.domain.exception.NotExistMatchSubscriptionException;
import com.sunghyun.plab.subscription.domain.model.MatchSubscription;
import com.sunghyun.plab.subscription.domain.service.MatchSubscriptionDomainService;
import com.sunghyun.utils.ApiUtils;
import com.sunghyun.web.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MatchSubscriptionService implements MatchSubscriptionUseCase {
    private final MatchSubscriptionDomainService matchSubscriptionDomainService;
    private final MatchSubscriptionRepository matchSubscriptionRepository;
    private final PlabMatchOutPort plabMatchOutPort;

    public MatchSubscriptionRegResDto registerMatchSubscription(final MatchSubscriptionRegReqDto dto){
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
        matchSubscriptionRepository.findMatchSubscriptionByMemberNoAndPlabMatchNo(dto.getMemberNo(), dto.getPlabMatchNo())
                .ifPresent(m -> {
                    throw new ExistMatchSubscriptionException(ErrorCode.P01);
                })
        ;

        //매치 구독 등록
        MatchSubscription matchSubscription = matchSubscriptionDomainService.createMatchSubscription(
                dto.getPlabMatchNo(),
                dto.getMemberNo(),
                dto.getEmail(),
                dto.getTargetPlayerCnt(),
                dto.getSubType()
        );

        //도메인 서비스 내에서 영속화되는 게 아닌 애플리케이션 레이어에서 해야 하지 않나? 얼추 맞는 말이다. 대신, 트랜잭션 분리 여부를 확인해야 한다.
//        저장
        MatchSubscription savedMatchSubscription = matchSubscriptionRepository.save(matchSubscription);

        //알림 발송(조건에 부합한다면 알림 발송)
        //이게 비즈니스 로직에 영향을 미쳐선 안된다
//        sendMail(??)
        
        return MatchSubscriptionRegResDto.from(savedMatchSubscription,result);
    }

    @Transactional
    public MatchSubscriptionModResDto modifyMatchSubscription(final Long subscriptionNo, final MatchSubscriptionModReqDto dto){
        MatchSubscription selectedMatchSubscription = matchSubscriptionRepository.getMatchSubscriptionBySubscriptionNo(subscriptionNo)
                .orElseThrow(()->new NotExistMatchSubscriptionException(ErrorCode.P03))
                ;
        MatchSubscription modifyReqMatchSubscription = dto.toDomain();

        //업데이트 여부 플래그
        boolean isUpdated = ApiUtils.merge(modifyReqMatchSubscription,selectedMatchSubscription);
        if(isUpdated){
            //새로 변경했으모로 새롭게 알림 받을 수 있드록 false 수정
            selectedMatchSubscription.resetNotification();
            matchSubscriptionRepository.save(selectedMatchSubscription);
        }

        return MatchSubscriptionModResDto.from(selectedMatchSubscription);
    }
}
