package com.sunghyun.schedule.application.dto.res;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sunghyun.feign.dto.InternalGuestRecruitmentDto;
import com.sunghyun.schedule.domain.PollAttendance;
import com.sunghyun.schedule.domain.SchedulePoll;
import com.sunghyun.schedule.domain.enums.AttendanceStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Builder
@AllArgsConstructor
public class SchedulePollResDto {
    private Long schedulePollNo;
    private Long teamNo;
    private String title;

    // 🟢 경기 정보 그룹핑 DTO
    private MatchInfoResDto matchInfo;
    private InternalGuestRecruitmentDto guestRecruitmentInfo;

    @JsonProperty("isClosed")
    private boolean isClosed;

    private Integer minPlayer;
    private Integer maxPlayer;

    private Integer expectedAllCost;
    private Integer expectedCostPerPerson;

    private List<PollAttendanceResDto> attendees;   // 참석
    private List<PollAttendanceResDto> absentees;   // 불참
    private List<PollAttendanceResDto> undecideds;  // 미정

    private AttendanceStatus myVoteStatus;

    private String closedDt;
    private String closedTm;

    public static SchedulePollResDto from(SchedulePoll schedulePoll, Long myTeamMemberNo, InternalGuestRecruitmentDto guestRecruitmentInfo) {
        var playerLimit = schedulePoll.getPlayerLimit();
        var costInfo = schedulePoll.getCostInfo();

        List<PollAttendance> attendances = schedulePoll.getAttendances() == null
                ? Collections.emptyList()
                : schedulePoll.getAttendances();

        // 1. 상태별 그룹핑
        Map<AttendanceStatus, List<PollAttendanceResDto>> grouped = attendances.stream()
                .map(PollAttendanceResDto::from)
                .collect(Collectors.groupingBy(PollAttendanceResDto::getAttendanceStatus));

        // 2. 내 투표 상태 추출
        AttendanceStatus myVoteStatus = schedulePoll.getAttendanceStatusOf(myTeamMemberNo);

        return SchedulePollResDto.builder()
                .schedulePollNo(schedulePoll.getSchedulePollNo())
                .teamNo(schedulePoll.getTeamNo())
                .title(schedulePoll.getTitle())

                // 🟢 MatchInfoResDto 정적 팩토리 메서드로 매핑
                .matchInfo(MatchInfoResDto.from(schedulePoll.getMatchInfo()))
                .guestRecruitmentInfo(guestRecruitmentInfo)
                .isClosed(schedulePoll.isClosed())
                .minPlayer(playerLimit != null ? playerLimit.getMin() : null)
                .maxPlayer(playerLimit != null ? playerLimit.getMax() : null)
                .expectedAllCost(costInfo != null ? costInfo.getExpectedTotalCost() : 0)
                .expectedCostPerPerson(costInfo != null ? costInfo.calculateCostPerPerson(grouped.getOrDefault(AttendanceStatus.ATTENDING, List.of()).size()) : 0)

                .attendees(grouped.getOrDefault(AttendanceStatus.ATTENDING, Collections.emptyList()))
                .absentees(grouped.getOrDefault(AttendanceStatus.ABSENT, Collections.emptyList()))
                .undecideds(grouped.getOrDefault(AttendanceStatus.UNDECIDED, Collections.emptyList()))

                .myVoteStatus(myVoteStatus)
                .closedDt(schedulePoll.getClosedDt())
                .closedTm(schedulePoll.getClosedTm())
                .build();
    }
}