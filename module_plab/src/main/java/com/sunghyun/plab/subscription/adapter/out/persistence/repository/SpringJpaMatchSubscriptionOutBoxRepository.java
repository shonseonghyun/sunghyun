package com.sunghyun.plab.subscription.adapter.out.persistence.repository;

import com.sunghyun.plab.subscription.adapter.out.persistence.entity.MatchSubscriptionOutBoxEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringJpaMatchSubscriptionOutBoxRepository extends JpaRepository<MatchSubscriptionOutBoxEntity,String> {
}
