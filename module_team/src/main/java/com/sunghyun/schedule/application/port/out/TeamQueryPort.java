package com.sunghyun.schedule.application.port.out;

import com.sunghyun.team.domain.model.TeamMember;

import java.util.List;
import java.util.Optional;

public interface TeamQueryPort {
    boolean isTeamLeader(Long teamNo,Long memberNo);
    boolean isTeamMember(Long teamNo,Long memberNo);
    Long getTeamMemberNo(Long teamNo, Long memberNo);
    List<TeamMember> getTeamMembers(Long teamNo);
    Optional<Long> getTeamNoByMemberNo(Long memberNo);
    Optional<Long> getTeamMemberNoByMemberNo(java.lang.Long memberNo);
}
