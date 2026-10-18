package com.sunghyun.team.domain.model;

import com.sunghyun.team.domain.enums.AgeGroup;
import com.sunghyun.team.domain.enums.GenderComposition;
import com.sunghyun.team.domain.enums.TeamMemberRole;
import com.sunghyun.team.domain.enums.TeamType;
import com.sunghyun.team.domain.exceptions.TeamJoinRequestException;
import com.sunghyun.web.ErrorCode;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class Team {
    private Long teamNo;

    private Long creatorMemberNo;

    private String teamName;                     // 팀명
    private TeamType teamType;                   // 풋살, 축구
    private String logoUrl;                      // 로고(선택)
    private String teamDesc;                     // 팀 소개(선택)
    private String activityRegion;               // 활동 지역
    private String frequentStadium;              // 자주 구장 (선택)
    private GenderComposition genderComposition; // 성별 구성
    private AgeGroup mainAgeGroup;               // 주 연령대
    private MonthlyFee monthlyFee;               // 월 회비 (선택 - null 가능)
    private List<TeamMember> teamMemberList;

    public static Team createTeam(Long creatorMemberNo, String teamName, TeamType teamType, String logoUrl, String teamDesc, String activityRegion, String frequentStadium, GenderComposition genderComposition, AgeGroup mainAgeGroup,MonthlyFee monthlyFee) {
        // 생성한 사람은 리더
        List<TeamMember> newTeamMemberList = new ArrayList<>();
        TeamMember newLeader = TeamMember.createLeader(creatorMemberNo);
        newTeamMemberList.add(newLeader);

        return Team.builder()
                .creatorMemberNo(creatorMemberNo)
                .teamName(teamName)
                .teamType(teamType)
                .logoUrl(logoUrl)
                .activityRegion(activityRegion)
                .frequentStadium(frequentStadium)
                .genderComposition(genderComposition)
                .mainAgeGroup(mainAgeGroup)
                .teamDesc(teamDesc)
                .monthlyFee(monthlyFee)
                .teamMemberList(newTeamMemberList)
                .build();

    }

    public boolean isMember(Long memberNo) {
        return this.teamMemberList.stream()
                .anyMatch(teamMember -> teamMember.getRealMemberNo().equals(memberNo))
                ;
    }

    // 회장 검증
    public void validateLeader(Long leaderMemberNo) {
        if (!isLeader(leaderMemberNo)) {
            throw new TeamJoinRequestException(ErrorCode.T006);
        }
    }

    private boolean isLeader(Long leaderMemberNo) {
        return this.teamMemberList.stream()
                .filter(teamMember -> teamMember.getRealMemberNo().equals(leaderMemberNo))
                .anyMatch(teamMember -> teamMember.getTeamMemberRole().equals(TeamMemberRole.LEADER))
                ;
    }

    public void addMember(Long newMemberNo) {
        this.teamMemberList.add(TeamMember.createGeneralMember(newMemberNo));
    }
}
