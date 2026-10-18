package com.sunghyun.team.domain.model;

import com.sunghyun.team.domain.enums.TeamMemberRole;
import com.sunghyun.utils.ApiUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class TeamMember {
    private Long teamMemberNo;
    private Long realMemberNo;
    private TeamMemberRole teamMemberRole;

    //회원 프로필 사진
    //출석률
    //POM 횟수

    private String createdDt;
    private String createdTm;

    public static TeamMember createLeader(Long newMemberNo){
        return TeamMember.builder()
                .realMemberNo(newMemberNo)
                .teamMemberRole(TeamMemberRole.LEADER)
                .createdDt(ApiUtils.getCurrentDt())
                .createdTm(ApiUtils.getCurrentTm())
                .build()
                ;
    }

    public static TeamMember createGeneralMember(Long newMemberNo){
        return TeamMember.builder()
                .realMemberNo(newMemberNo)
                .teamMemberRole(TeamMemberRole.GENERAL)
                .createdDt(ApiUtils.getCurrentDt())
                .createdTm(ApiUtils.getCurrentTm())
                .build()
                ;
    }
}
