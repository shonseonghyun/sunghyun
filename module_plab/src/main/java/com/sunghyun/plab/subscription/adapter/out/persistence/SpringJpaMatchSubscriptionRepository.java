package com.sunghyun.plab.subscription.adapter.out.persistence;

import com.sunghyun.plab.subscription.domain.model.MatchSubscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpringJpaMatchSubscriptionRepository extends JpaRepository<MatchSubscription,Long> {
    Optional<MatchSubscription> findMatchSubscriptionByMemberNoAndPlabMatchNo(final Long memberNo, final Long plabMatchNo);
}
