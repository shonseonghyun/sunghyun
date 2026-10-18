package com.sunghyun.team.application.port.out.dto;

import com.sunghyun.team.domain.model.Team;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class TeamCreateResDto {
    private Long teamNo;
    private String teamName;

    public static TeamCreateResDto from(Team team) {
        return TeamCreateResDto.builder()
                .teamNo(team.getTeamNo())
                .teamName(team.getTeamName())
                .build();
    }
}