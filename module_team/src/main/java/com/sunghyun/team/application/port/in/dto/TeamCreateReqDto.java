package com.sunghyun.team.application.port.in.dto;

import com.sunghyun.team.domain.enums.AgeGroup;
import com.sunghyun.team.domain.enums.GenderComposition;
import com.sunghyun.team.domain.enums.TeamType;
import com.sunghyun.team.domain.model.MonthlyFee;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
public class TeamCreateReqDto {
    private String teamName;
    private TeamType teamType;            // 풋살 / 축구
    private String logoUrl;                      // 로고(선택)
    private String teamDesc;                     // 팀 소개(선택)
    private String activityRegion;               // 활동 지역
    private String frequentStadium;              // 자주 구장 (선택)
    private GenderComposition genderComposition; // 성별 구성
    private AgeGroup mainAgeGroup;               // 주 연령대
    private MonthlyFee monthlyFee;               // 월 회비 (선택 - null 가능)
}
