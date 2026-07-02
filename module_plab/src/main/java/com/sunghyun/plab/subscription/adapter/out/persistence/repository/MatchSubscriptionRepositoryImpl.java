package com.sunghyun.plab.subscription.adapter.out.persistence.repository;

import com.sunghyun.plab.subscription.adapter.out.persistence.MatchSubscriptionMapper;
import com.sunghyun.plab.subscription.adapter.out.persistence.entity.MatchSubscriptionEntity;
import com.sunghyun.plab.subscription.application.port.out.persistence.MatchSubscriptionRepository;
import com.sunghyun.plab.subscription.domain.enums.NotiType;
import com.sunghyun.plab.subscription.domain.model.MatchSubscription;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MatchSubscriptionRepositoryImpl implements MatchSubscriptionRepository {
    private final MatchSubscriptionMapper mapper;
    private final SpringJpaMatchSubscriptionRepository springJpaMatchSubscriptionRepository;

    @Override
    public List<MatchSubscription> getMatchSubscriptions(final Long memberNo, List<Long> plabMatchNos) {
        List<MatchSubscriptionEntity> entities = springJpaMatchSubscriptionRepository.findByMemberNoAndPlabMatchNoIn(memberNo,plabMatchNos);
        return entities.stream()
                .map(mapper::toDomain)
                .toList()
                ;
    }

    @Override
    public Optional<MatchSubscription> getMatchSubscriptionBySubscriptionNo(final Long subscriptionNo) {
        return springJpaMatchSubscriptionRepository.findById(subscriptionNo)
                .map(mapper::toDomain)
                ;
    }

    @Override
    public Optional<MatchSubscription> findMatchSubscriptionByMemberNoAndPlabMatchNoAndNotiType(final Long memberNo, final Long plabMatchNo, final NotiType notiType) {
        return springJpaMatchSubscriptionRepository.findMatchSubscriptionByMemberNoAndPlabMatchNoAndNotiType(memberNo,plabMatchNo,notiType)
                .map(mapper::toDomain)
                ;
    }

    @Override
    public MatchSubscription save(final MatchSubscription matchSubscription) {
        return mapper.toDomain(springJpaMatchSubscriptionRepository.save(mapper.toEntity(matchSubscription)))
                ;
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
