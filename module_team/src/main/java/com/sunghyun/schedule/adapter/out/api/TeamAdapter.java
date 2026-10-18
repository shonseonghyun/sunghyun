package com.sunghyun.schedule.adapter.out.api;

import com.sunghyun.schedule.application.port.out.TeamQueryPort;
import com.sunghyun.team.domain.model.Team;
import com.sunghyun.team.domain.model.TeamMember;
import com.sunghyun.team.domain.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TeamAdapter implements TeamQueryPort {
    private final TeamRepository teamRepository;

    @Override
    public boolean isTeamLeader(Long teamNo,Long memberNo) {
        return true;
    }

    @Override
    public boolean isTeamMember(Long teamNo,Long memberNo) {
        return true;
    }

    @Override
    public Long getTeamMemberNo(Long teamNo, Long memberNo) {
        return teamRepository.getTeamMemberNoByRealMemberNo(teamNo,memberNo);
    }

    @Override
    public List<TeamMember> getTeamMembers(Long teamNo) {
        return teamRepository.findTeamMembersByTeamNo(teamNo);
    }

    @Override
    public Optional<Long> getTeamNoByMemberNo(Long memberNo) {
        return teamRepository.findByMemberNo(memberNo)
                .map(Team::getTeamNo)
                ;
    }

    @Override
    public Optional<Long> getTeamMemberNoByMemberNo(Long memberNo) {
        return teamRepository.findByMemberNo(memberNo)
                .flatMap(team -> team.getTeamMemberList().stream()
                        .filter(teamMember -> teamMember.getRealMemberNo().equals(memberNo))
                        .map(TeamMember::getTeamMemberNo)
                        .findFirst());
    }
}
