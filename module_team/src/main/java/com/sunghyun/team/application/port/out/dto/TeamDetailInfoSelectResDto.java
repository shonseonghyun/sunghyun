package com.sunghyun.team.application.port.out.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sunghyun.schedule.application.dto.res.LocationResDto;
import com.sunghyun.schedule.domain.Location;
import com.sunghyun.team.domain.enums.AgeGroup;
import com.sunghyun.team.domain.enums.GenderComposition;
import com.sunghyun.team.domain.enums.TeamType;
import com.sunghyun.team.domain.model.Team;
import lombok.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TeamDetailInfoSelectResDto {

    private Long teamNo;
    private Long creatorMemberNo;
    private String teamName;
    private TeamType teamType;
    private String logoUrl;
    private String teamDesc;
    private String activityRegion;
    private LocationResDto location;
    private GenderComposition genderComposition;
    private AgeGroup mainAgeGroup;

    private MonthlyFeeResDto monthlyFee;
    private List<TeamMemberResDto> members;

    @JsonProperty(value = "isTeamMember")
    private boolean isTeamMember;

    /**
     * Team 도메인 객체로부터 DTO 생성
     */
    public static TeamDetailInfoSelectResDto from(Team team,boolean isTeamMember,Location location) {
        MonthlyFeeResDto feeDto = null;
        if (team.getMonthlyFee() != null) {
            feeDto = MonthlyFeeResDto.builder()
                    .amount(team.getMonthlyFee().getAmount())
                    .paymentDay(team.getMonthlyFee().getPaymentDay())
                    .build();
        }

        List<TeamMemberResDto> memberDtos = (team.getTeamMemberList() != null) ?
                team.getTeamMemberList().stream()
                        .map(TeamMemberResDto::from)
                        .collect(Collectors.toList()) : Collections.emptyList();

        LocationResDto locationResDto = location == null ? null : LocationResDto.from(location);

        return TeamDetailInfoSelectResDto.builder()
                .teamNo(team.getTeamNo())
                .creatorMemberNo(team.getCreatorMemberNo())
                .teamName(team.getTeamName())
                .teamType(team.getTeamType())
                .logoUrl(team.getLogoUrl())
                .teamDesc(team.getTeamDesc())
                .activityRegion(team.getActivityRegion())
                .location(locationResDto)
                .genderComposition(team.getGenderComposition())
                .mainAgeGroup(team.getMainAgeGroup())
                .monthlyFee(feeDto)
                .members(memberDtos)
                .isTeamMember(isTeamMember)
                .build();
    }
}