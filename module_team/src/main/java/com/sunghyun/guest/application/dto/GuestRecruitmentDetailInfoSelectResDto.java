package com.sunghyun.guest.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sunghyun.feign.dto.InternalSchedulePollResponseDto;
import com.sunghyun.feign.dto.InternalTeamResponseDto;
import com.sunghyun.guest.domain.*;
import com.sunghyun.schedule.application.dto.res.LocationResDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GuestRecruitmentDetailInfoSelectResDto {
    private Long recruitmentNo;
    private String title;
    private RecruitmentStatus status;
    private List<PositionRequirementResDto> requirements;
    private LevelLimit levelLimit;
    private MatchFormat matchFormat;
    private int guestFee;
    private int quarterDuration;
    private int restDuration;
    private int minGuaranteedQuarter;

    @JsonProperty("isBeverageProvided")
    private boolean beverageProvided;
    private String additionalNotice;
    private String createdDt;
    private String createdTm;

    @JsonProperty("isApplied")
    private boolean applied;

    // 팀 정보
    private Long teamNo;
    private Long creatorMemberNo;
    private String teamName;
    private String logoUrl;
    private String teamDesc;

    // 스케줄 정보
    private String matchType;
    private LocationResDto location;
    private String playDt;
    private String playTm;

    public static GuestRecruitmentDetailInfoSelectResDto of(
            GuestRecruitment recruitment,
            InternalSchedulePollResponseDto scheduleInfo,
            InternalTeamResponseDto teamInfo,
            Long applyedMemberNo
    ) {
        List<PositionRequirementResDto> positionRequirementResDtos = recruitment.getRequirements().stream()
                .map(req -> {
                    int confirmedCount = recruitment.getConfirmedCountByPosition(req.getPosition());
                    return PositionRequirementResDto.of(req, confirmedCount);
                })
                .toList();

        return GuestRecruitmentDetailInfoSelectResDto.builder()
                .recruitmentNo(recruitment.getRecruitmentNo())
                .title(recruitment.getTitle()) // 팀명 기반 기본 타이틀 조합
                .status(recruitment.getStatus())
                .requirements(positionRequirementResDtos)
                .levelLimit(recruitment.getLevelLimit())
                .matchFormat(recruitment.getMatchFormat())
                .guestFee(recruitment.getGuestFee())
                .quarterDuration(recruitment.getQuarterDuration())
                .restDuration(recruitment.getRestDuration())
                .minGuaranteedQuarter(recruitment.getMinGuaranteedQuarter())
                .beverageProvided(recruitment.isBeverageProvided())
                .additionalNotice(recruitment.getAdditionalNotice())
                .createdDt(recruitment.getCreatedDt())
                .createdTm(recruitment.getCreatedTm())
                .applied(recruitment.isApplied(applyedMemberNo))
                // 팀 정보
                .teamNo(teamInfo.getTeamNo())
                .creatorMemberNo(teamInfo.getCreatorMemberNo())
                .teamName(teamInfo.getTeamName())
                .logoUrl(teamInfo.getLogoUrl())
                .teamDesc(teamInfo.getTeamDesc())
                // 스케줄 정보
                .matchType(scheduleInfo.getMatchType())
                .location(LocationResDto.from(scheduleInfo.getLocation()))
                .playDt(scheduleInfo.getPlayDt())
                .playTm(scheduleInfo.getPlayTm())
                .build();
    }
}