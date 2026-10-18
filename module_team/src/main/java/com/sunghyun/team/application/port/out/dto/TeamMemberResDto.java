package com.sunghyun.team.application.port.out.dto;

import com.sunghyun.team.domain.enums.TeamMemberRole;
import com.sunghyun.team.domain.model.TeamMember;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TeamMemberResDto {
    private Long teamMemberNo;
    private Long realMemberNo;
    private TeamMemberRole teamMemberRole;
    private String createdDt;
    private String createdTm;

    public static TeamMemberResDto from(TeamMember teamMember) {
        return TeamMemberResDto.builder()
                .teamMemberNo(teamMember.getTeamMemberNo())
                .realMemberNo(teamMember.getRealMemberNo())
                .teamMemberRole(teamMember.getTeamMemberRole())
                .createdDt(teamMember.getCreatedDt())
                .createdTm(teamMember.getCreatedTm())
                .build();
    }
}