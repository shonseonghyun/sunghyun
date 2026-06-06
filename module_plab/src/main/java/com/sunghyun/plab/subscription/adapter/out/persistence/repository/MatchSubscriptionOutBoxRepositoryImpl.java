package com.sunghyun.plab.subscription.adapter.out.persistence.repository;

import com.sunghyun.plab.subscription.adapter.out.persistence.entity.MatchSubscriptionOutBoxEntity;
import com.sunghyun.plab.subscription.application.port.out.persistence.MatchSubscriptionOutBoxRepository;
import com.sunghyun.plab.subscription.domain.model.MatchSubscriptionOutBox;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class MatchSubscriptionOutBoxRepositoryImpl implements MatchSubscriptionOutBoxRepository {
    private final SpringJpaMatchSubscriptionOutBoxRepository springJpaMatchSubscriptionOutBoxRepository;

    @Override
    public MatchSubscriptionOutBox save(MatchSubscriptionOutBox outBox) {
        return springJpaMatchSubscriptionOutBoxRepository
                .save(MatchSubscriptionOutBoxEntity.from(outBox))
                .toDomain();
    }

    @Override
    public Optional<MatchSubscriptionOutBox> findById(String outBoxNo) {
        return springJpaMatchSubscriptionOutBoxRepository.findById(outBoxNo)
                .map(MatchSubscriptionOutBoxEntity::toDomain)
                ;
    }
}
