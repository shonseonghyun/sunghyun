package com.sunghyun.team.application.port.out.dto;

import com.sunghyun.team.domain.model.Team;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeamSimpleInfoSelectResDto {
    private Long teamNo;
    private String teamName;
    private String logoUrl;
    private String teamDesc;

    public static TeamSimpleInfoSelectResDto from(Team selectedTeam) {
        return TeamSimpleInfoSelectResDto.builder()
                .teamNo(selectedTeam.getTeamNo())
                .teamName(selectedTeam.getTeamName())
                .logoUrl(selectedTeam.getLogoUrl())
                .teamDesc(selectedTeam.getTeamDesc())
                .build();
    }
}