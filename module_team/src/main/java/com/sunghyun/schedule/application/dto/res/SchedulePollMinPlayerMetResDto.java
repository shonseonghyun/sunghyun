package com.sunghyun.schedule.application.dto.res;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sunghyun.schedule.domain.SchedulePoll;
import com.sunghyun.schedule.domain.enums.MatchType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class SchedulePollMinPlayerMetResDto {
    private Long schedulePollNo;

    private Long teamNo;

    // 2. 제목
    private String title;

    private MatchType matchType;
    private LocationResDto location;

    // 4. 경기 일자 & 시간
    private String playDt;
    private String playTm;

    @JsonProperty("isClosed")
    private boolean isClosed;

    private Integer minPlayer;
    private Integer maxPlayer;

    private AttendanceCountDto attendanceCount;

    @JsonProperty("isMinPlayerMet")
    private boolean isMinPlayerMet;

    public static SchedulePollMinPlayerMetResDto from(SchedulePoll schedulePoll) {
        return SchedulePollMinPlayerMetResDto.builder()
                .schedulePollNo(schedulePoll.getSchedulePollNo())
                .teamNo(schedulePoll.getTeamNo())
                .title(schedulePoll.getTitle())
                .matchType(schedulePoll.getMatchInfo().getMatchType())
                .location(schedulePoll.getMatchInfo().getLocation() != null ? LocationResDto.from(schedulePoll.getMatchInfo().getLocation()) : null)
                .playDt(schedulePoll.getMatchInfo().getPlayDt())
                .playTm(schedulePoll.getMatchInfo().getPlayTm())
                .isClosed(schedulePoll.isClosed())
                .minPlayer(schedulePoll.getPlayerLimit().getMin())
                .maxPlayer(schedulePoll.getPlayerLimit().getMax())
                .attendanceCount(AttendanceCountDto.from(schedulePoll)) // 참석자 카운트 DTO 매핑
                .isMinPlayerMet(schedulePoll.isMinPlayerMet()) // 도메인 메서드 호출
                .build();
    }
}
