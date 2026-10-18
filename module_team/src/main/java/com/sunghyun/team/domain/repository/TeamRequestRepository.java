package com.sunghyun.team.domain.repository;

import com.sunghyun.team.domain.enums.TeamJoinRequestStatus;
import com.sunghyun.team.domain.model.TeamJoinRequest;

import java.util.Optional;

public interface TeamRequestRepository {
    Optional<TeamJoinRequest> findLatestTeamJoinRequest(Long teamNo, Long memberNo);
    TeamJoinRequest save(TeamJoinRequest teamJoinRequest);
    Optional<TeamJoinRequest> findByRequestNo(Long requestNo);
    boolean existJoinTeamRequested(Long requesterMemberNo,TeamJoinRequestStatus status);
    Optional<TeamJoinRequest> findPendingRequestByMemberNo(Long requesterMemberNo);
}
