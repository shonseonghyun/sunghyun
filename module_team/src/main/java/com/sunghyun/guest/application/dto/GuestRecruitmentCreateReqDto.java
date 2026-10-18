package com.sunghyun.guest.application.dto;

import com.sunghyun.guest.domain.LevelLimit;
import com.sunghyun.guest.domain.MatchFormat;
import com.sunghyun.guest.domain.PositionRequirement;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GuestRecruitmentCreateReqDto {
    @NotNull
    private Long teamNo;

    @NotNull
    private String title;

    @NotNull
    private List<PositionRequirement> requirements;

    @NotNull
    private LevelLimit levelLimit;

    @NotNull
    private MatchFormat matchFormat;

    @NotNull
    private int durationMinute;

    @NotNull
    private String playDt;

    @NotNull
    private String playTm;

    @NotNull
    private int guestFee;

    @NotNull
    private int minGuaranteedQuarter;

    @NotNull
    private int quarterDuration;

    @NotNull
    private int restDuration;

    @NotNull
    private boolean isBeverageProvided;

    private String additionalNotice;
}