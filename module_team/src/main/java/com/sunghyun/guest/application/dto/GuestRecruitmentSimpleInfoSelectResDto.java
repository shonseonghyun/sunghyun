package com.sunghyun.guest.application.dto;

import com.sunghyun.guest.domain.MatchFormat;
import com.sunghyun.guest.domain.RecruitmentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GuestRecruitmentSimpleInfoSelectResDto {
    private Long recruitmentNo;
    private String title;
    private RecruitmentStatus status;
    private List<PositionRequirementResDto> requirements;
    private MatchFormat matchFormat;
    private int guestFee;

    // 팀 정보
    private Long creatorMemberNo;
    private String teamName;
    private String logoUrl;

    // 스케줄 정보
    private String location;
    private String playDt;
    private String playTm;

}