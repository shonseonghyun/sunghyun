package com.sunghyun.plab.subscription.application.port.out.persistence;

import com.sunghyun.plab.subscription.adapter.out.persistence.MatchSubscriptionEntity;
import com.sunghyun.plab.subscription.domain.enums.NotiType;
import com.sunghyun.plab.subscription.domain.model.MatchSubscription;

import java.util.Optional;

public interface MatchSubscriptionRepository {
    Optional<MatchSubscription> getMatchSubscriptionBySubscriptionNo(final Long subscriptionNo);
    Optional<MatchSubscription> findMatchSubscriptionByMemberNoAndPlabMatchNoAndNotiType(final Long memberNo, final Long plabMatchNo, final NotiType notiType);
    MatchSubscription save(final MatchSubscription matchSubscription);
    void deleteAll();
    Long count();
}
