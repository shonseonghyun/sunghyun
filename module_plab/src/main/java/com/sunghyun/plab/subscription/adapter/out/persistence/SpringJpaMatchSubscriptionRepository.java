package com.sunghyun.plab.subscription.adapter.out.persistence;

import com.sunghyun.plab.subscription.domain.enums.NotiType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpringJpaMatchSubscriptionRepository extends JpaRepository<MatchSubscriptionEntity,Long> {
    Optional<MatchSubscriptionEntity> findMatchSubscriptionByMemberNoAndPlabMatchNoAndNotiType(final Long memberNo, final Long plabMatchNo, final NotiType notiType);
}
