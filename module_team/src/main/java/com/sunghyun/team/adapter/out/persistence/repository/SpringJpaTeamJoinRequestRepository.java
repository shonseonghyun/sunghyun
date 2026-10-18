package com.sunghyun.team.adapter.out.persistence.repository;

import com.sunghyun.team.adapter.out.persistence.entity.TeamJoinRequestEntity;
import com.sunghyun.team.domain.enums.TeamJoinRequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpringJpaTeamJoinRequestRepository extends JpaRepository<TeamJoinRequestEntity, Long> {
    Optional<TeamJoinRequestEntity> findFirstByTeamNoAndMemberNoOrderByRequestNoDesc(Long teamNo, Long memberNo);
    boolean existsByMemberNoAndStatus(Long memberNo, TeamJoinRequestStatus status);
    Optional<TeamJoinRequestEntity> findFirstByMemberNoAndStatusOrderByRequestNoDesc(Long memberNo, TeamJoinRequestStatus status);
}