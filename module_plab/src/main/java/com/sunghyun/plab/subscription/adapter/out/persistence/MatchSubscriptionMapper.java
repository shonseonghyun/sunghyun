package com.sunghyun.plab.subscription.adapter.out.persistence;

import com.sunghyun.plab.subscription.domain.model.MatchSubscription;
import org.springframework.stereotype.Component;

@Component
public class MatchSubscriptionMapper {

    /**
     * Domain -> Entity (저장/수정 시 사용)
     */
    public MatchSubscriptionEntity toEntity(MatchSubscription domain) {
        return MatchSubscriptionEntity.builder()
                .subscriptionNo(domain.getSubscriptionNo())
                .plabMatchNo(domain.getPlabMatchNo())
                .memberNo(domain.getMemberNo())
                .email(domain.getEmail())
                .notiType(domain.getNotiType())
                .notiValue(domain.getNotiValue())
                .build();
    }

    /**
     * Entity -> Domain (DB 조회 시 사용)
     */
    public MatchSubscription toDomain(MatchSubscriptionEntity entity) {
        // NotiType 내부의 전략(convert)을 사용하여 숫자를 다시 객체로 복원
        return MatchSubscription.builder()
                .subscriptionNo(entity.getSubscriptionNo())
                .plabMatchNo(entity.getPlabMatchNo())
                .memberNo(entity.getMemberNo())
                .email(entity.getEmail())
                .notiType(entity.getNotiType())
                .notiValue(entity.getNotiValue()) // Integer -> NotiValue 복원
                .build();
    }
}