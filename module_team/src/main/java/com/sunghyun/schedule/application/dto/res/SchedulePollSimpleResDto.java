package com.sunghyun.schedule.application.dto.res;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sunghyun.schedule.domain.SchedulePoll;
import com.sunghyun.schedule.domain.enums.AttendanceStatus; // 참석/불참/미정 enum 예시
import com.sunghyun.schedule.domain.enums.MatchType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class SchedulePollSimpleResDto {

    // 1. PK
    private Long schedulePollNo;

    private Long teamNo;

    // 2. 제목
    private String title;

    // 3. 경기 타입 & 위치 정보
    private MatchType matchType;
    private String location;

    // 4. 경기 일자 & 시간
    private String playDt;
    private String playTm;

    // 5. 마감 여부
    @JsonProperty("isClosed")
    private boolean isClosed;

    // 6. 최소 / 최대 인원수
    private Integer minPlayer;
    private Integer maxPlayer;

    // 7. 투표 참석 집계 정보 (참석, 불참, 미정)
    private AttendanceCountDto attendanceCount;

    // 8. 내가 선택한 투표 상태 (참석 / 불참 / 미정 / null)
    private AttendanceStatus myAttendanceStatus;

    /**
     * SchedulePoll 도메인 객체와 현재 로그인 유저 식별자(myMemberNo)를 받아 DTO로 변환
     */
    public static SchedulePollSimpleResDto from(SchedulePoll schedulePoll, Long myTeamMemberNo) {
        AttendanceStatus myStatus = schedulePoll.getAttendanceStatusOf(myTeamMemberNo);

        return SchedulePollSimpleResDto.builder()
                .schedulePollNo(schedulePoll.getSchedulePollNo())
                .teamNo(schedulePoll.getTeamNo())
                .title(schedulePoll.getTitle())
                .matchType(schedulePoll.getMatchInfo() != null ? schedulePoll.getMatchInfo().getMatchType() : null)
                .location(schedulePoll.getMatchInfo().getLocation() != null ? schedulePoll.getMatchInfo().getLocation().getLocationName() : null)
                .playDt(schedulePoll.getMatchInfo() != null ? schedulePoll.getMatchInfo().getPlayDt() : null)
                .playTm(schedulePoll.getMatchInfo() != null ? schedulePoll.getMatchInfo().getPlayTm() : null)
                .isClosed(schedulePoll.isClosed())
                .minPlayer(schedulePoll.getPlayerLimit() != null ? schedulePoll.getPlayerLimit().getMin() : null)
                .maxPlayer(schedulePoll.getPlayerLimit() != null ? schedulePoll.getPlayerLimit().getMax() : null)
                .attendanceCount(AttendanceCountDto.from(schedulePoll))
                .myAttendanceStatus(myStatus)
                .build();
    }
}