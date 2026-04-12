package com.sunghyun.plab.subscription.adapter.out.persistence;

import com.sunghyun.plab.subscription.domain.model.MatchSubscription;
import com.sunghyun.plab.subscription.application.port.out.persistence.MatchSubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MatchSubscriptionRepositoryImpl implements MatchSubscriptionRepository {
    private final SpringJpaMatchSubscriptionRepository springJpaMatchSubscriptionRepository;

    @Override
    public Optional<MatchSubscription> getMatchSubscriptionBySubscriptionNo(Long subscriptionNo) {
        return springJpaMatchSubscriptionRepository.findById(subscriptionNo);
//        return Optional.empty();
    }

    @Override
    public Optional<MatchSubscription> findMatchSubscriptionByMemberNoAndPlabMatchNo(final Long memberNo, final Long plabMatchNo) {
        return springJpaMatchSubscriptionRepository.findMatchSubscriptionByMemberNoAndPlabMatchNo(memberNo,plabMatchNo);
    }

    @Override
    public MatchSubscription save(final MatchSubscription matchSubscription) {
        return springJpaMatchSubscriptionRepository.save(matchSubscription);
    }

    @Override
    public void deleteAll() {
        springJpaMatchSubscriptionRepository.deleteAll();
    }

    @Override
    public Long count() {
        return springJpaMatchSubscriptionRepository.count();
    }
}
