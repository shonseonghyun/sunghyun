package com.sunghyun.plab.subscription.application.port.out.persistence;

import com.sunghyun.plab.subscription.domain.model.MatchSubscriptionOutBox;

import java.util.Optional;

public interface MatchSubscriptionOutBoxRepository {
    MatchSubscriptionOutBox save(final MatchSubscriptionOutBox outBox);
    Optional<MatchSubscriptionOutBox> findById(final String outBoxNo);
}
