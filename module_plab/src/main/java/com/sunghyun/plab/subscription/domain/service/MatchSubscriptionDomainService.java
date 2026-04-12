package com.sunghyun.plab.subscription.domain.service;

import com.sunghyun.plab.subscription.domain.enums.ActiveSubType;
import com.sunghyun.plab.subscription.domain.model.MatchSubscription;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MatchSubscriptionDomainService {
    //도메인 레이어가 애플리케이션에 의존함으로 해당 포트는 빠져야 한다.
//    private final MatchSubscriptionRepository matchSubscriptionRepository;

    @Transactional
    public MatchSubscription createMatchSubscription(
            final Long plabMatchNo,
            final Long memberNo,
            final String email,
            final Integer targetPlayerCnt,
            final ActiveSubType subType
    )
    {
        //구독 매치 등록 검증
//        matchSubscriptionRepository.findMatchSubscriptionByMemberNoAndPlabMatchNo(memberNo,plabMatchNo)
//                .ifPresent(m -> {
//                    throw new ExistMatchSubscriptionException(ErrorCode.P01);
//                })
//        ;

        //구독 정보 생성 및 저장
        MatchSubscription matchSubscription = MatchSubscription.create(
                plabMatchNo,
                memberNo,
                email,
                targetPlayerCnt,
                subType
        );
        return matchSubscription;

//        return matchSubscriptionRepository.save(matchSubscription);
    }
}
