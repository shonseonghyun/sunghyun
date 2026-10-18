package com.sunghyun.schedule.application.dto.res;

import com.sunghyun.schedule.domain.PollAttendance;
import com.sunghyun.schedule.domain.enums.AttendanceStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class PollAttendanceResDto {
    private Long pollAttendanceNo;

    private Long teamMemberNo;

    private AttendanceStatus attendanceStatus;

    public static PollAttendanceResDto from(PollAttendance pollAttendance) {
        if (pollAttendance == null) {
            return null;
        }

        return PollAttendanceResDto.builder()
                .pollAttendanceNo(pollAttendance.getPollAttendanceNo())
                .teamMemberNo(pollAttendance.getTeamMemberNo())
                .attendanceStatus(pollAttendance.getAttendanceStatus())
                .build();
    }
}
