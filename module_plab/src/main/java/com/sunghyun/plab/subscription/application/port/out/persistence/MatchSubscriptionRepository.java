package com.sunghyun.plab.subscription.application.port.out.persistence;

import com.sunghyun.plab.subscription.domain.model.MatchSubscription;

import java.util.Optional;

public interface MatchSubscriptionRepository {
    Optional<MatchSubscription> getMatchSubscriptionBySubscriptionNo(final Long subscriptionNo);
    Optional<MatchSubscription> findMatchSubscriptionByMemberNoAndPlabMatchNo(final Long memberNo, final Long plabMatchNo);
    MatchSubscription save(final MatchSubscription matchSubscription);
    void deleteAll();
    Long count();
}
