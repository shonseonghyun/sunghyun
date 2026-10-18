package com.sunghyun.team.domain.repository;

import com.sunghyun.team.domain.model.Team;
import com.sunghyun.team.domain.model.TeamMember;

import java.util.List;
import java.util.Optional;

public interface TeamRepository {
    Team save(Team team);
    Optional<Team> findByTeamNo(Long teamNo);
    boolean existJoinedTeamByMemberNo(Long requesterMemberNo);
    Optional<Team> findByMemberNo(Long memberNo);
    Long getTeamMemberNoByRealMemberNo(Long teamNo, Long memberNo);
    List<TeamMember> findTeamMembersByTeamNo(Long teamNo);
}
