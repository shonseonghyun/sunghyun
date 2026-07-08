package com.sunghyun.plab.subscription.adapter.out.persistence.repository;

import com.sunghyun.plab.subscription.adapter.out.persistence.entity.MatchSubscriptionEntity;
import com.sunghyun.plab.subscription.domain.enums.NotiType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SpringJpaMatchSubscriptionRepository extends JpaRepository<MatchSubscriptionEntity,Long> {
    Optional<MatchSubscriptionEntity> findMatchSubscriptionByMemberNoAndPlabMatchNoAndNotiType(final Long memberNo, final Long plabMatchNo, final NotiType notiType);
    List<MatchSubscriptionEntity> findByMemberNoAndPlabMatchNoIn(final Long memberNo, final List<Long> plabMatchNos);
    Optional<MatchSubscriptionEntity> findBySubscriptionNoAndMemberNo(final Long subscriptionNo, final Long memberNo);
}

