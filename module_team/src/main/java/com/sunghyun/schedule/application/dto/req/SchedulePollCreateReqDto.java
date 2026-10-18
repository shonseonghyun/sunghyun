package com.sunghyun.schedule.application.dto.req;

import com.sunghyun.schedule.domain.Location;
import com.sunghyun.schedule.domain.enums.MatchType;
import jakarta.annotation.Nullable;
import lombok.Getter;

@Getter
public class SchedulePollCreateReqDto {
    @Nullable
    private String title;

    private Long teamNo;

    private MatchType matchType;

    private Location location;

    private String playDt;
    private String playTm;

    @Nullable
    private Integer expectedAmount;

    private Integer min;
    @Nullable
    private Integer max;

    private String closedDt;
    private String closedTm;
}
