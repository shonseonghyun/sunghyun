package com.sunghyun.team.adapter.out.persistence.repository;

import com.sunghyun.team.adapter.out.persistence.entity.TeamEntity;
import com.sunghyun.team.adapter.out.persistence.entity.TeamMemberEntity;
import com.sunghyun.team.domain.model.Team;
import com.sunghyun.team.domain.model.TeamMember;
import com.sunghyun.team.domain.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TeamRepositoryImpl implements TeamRepository {

    private final SpringJpaTeamRepository springJpaTeamRepository;

    @Override
    public Team save(Team team) {
        return springJpaTeamRepository.save(TeamEntity.fromDomain(team))
                .toDomain();
    }

    @Override
    public Optional<Team> findByTeamNo(Long teamNo) {
        return springJpaTeamRepository.findById(teamNo)
                .map(TeamEntity::toDomain);
    }

    @Override
    public boolean existJoinedTeamByMemberNo(Long requesterMemberNo) {
        return springJpaTeamRepository.existsJoinedTeamByMemberNo(requesterMemberNo);
    }

    @Override
    public Optional<Team> findByMemberNo(Long memberNo) {
        return springJpaTeamRepository.findByMemberNo(memberNo)
                .map(TeamEntity::toDomain);
    }

    @Override
    public Long getTeamMemberNoByRealMemberNo(Long teamNo, Long memberNo) {
        return springJpaTeamRepository.findTeamMemberNoByTeamNoAndRealMemberNo(teamNo, memberNo);
    }

    @Override
    public List<TeamMember> findTeamMembersByTeamNo(Long teamNo) {
        return springJpaTeamRepository.findTeamMembersByTeamNo(teamNo)
                .stream()
                .map(TeamMemberEntity::toDomain)
                .toList();
    }
}