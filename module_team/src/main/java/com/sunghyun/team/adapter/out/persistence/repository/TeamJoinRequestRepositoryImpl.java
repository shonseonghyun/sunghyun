package com.sunghyun.team.adapter.out.persistence.repository;

import com.sunghyun.team.adapter.out.persistence.entity.TeamJoinRequestEntity;
import com.sunghyun.team.domain.enums.TeamJoinRequestStatus;
import com.sunghyun.team.domain.model.TeamJoinRequest;
import com.sunghyun.team.domain.repository.TeamRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TeamJoinRequestRepositoryImpl implements TeamRequestRepository {

    private final SpringJpaTeamJoinRequestRepository springJpaTeamJoinRequestRepository;

    @Override
    public Optional<TeamJoinRequest> findLatestTeamJoinRequest(Long teamNo, Long memberNo) {
        return springJpaTeamJoinRequestRepository.findFirstByTeamNoAndMemberNoOrderByRequestNoDesc(teamNo, memberNo)
                .map(TeamJoinRequestEntity::toDomain);
    }

    @Override
    public TeamJoinRequest save(TeamJoinRequest teamJoinRequest) {
        return springJpaTeamJoinRequestRepository.save(TeamJoinRequestEntity.fromDomain(teamJoinRequest))
                .toDomain();
    }

    @Override
    public Optional<TeamJoinRequest> findByRequestNo(Long requestNo) {
        return springJpaTeamJoinRequestRepository.findById(requestNo)
                .map(TeamJoinRequestEntity::toDomain);
    }

    @Override
    public boolean existJoinTeamRequested(Long requesterMemberNo, TeamJoinRequestStatus status) {
        return springJpaTeamJoinRequestRepository.existsByMemberNoAndStatus(requesterMemberNo, status);
    }

    @Override
    public Optional<TeamJoinRequest> findPendingRequestByMemberNo(Long requesterMemberNo) {
        return springJpaTeamJoinRequestRepository.findFirstByMemberNoAndStatusOrderByRequestNoDesc(requesterMemberNo, TeamJoinRequestStatus.REQUESTED)
                .map(TeamJoinRequestEntity::toDomain);
    }
}