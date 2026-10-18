package com.sunghyun.schedule.application.dto.res;

import com.sunghyun.schedule.domain.SchedulePoll;
import com.sunghyun.schedule.domain.enums.AttendanceStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AttendanceCountDto {
    private int attendCount;    // 참석
    private int absentCount;    // 불참
    private int undecidedCount; // 미정

    public static AttendanceCountDto from(SchedulePoll poll) {
        return new AttendanceCountDto(
                poll.getAttendanceCountByStatus(AttendanceStatus.ATTENDING),
                poll.getAttendanceCountByStatus(AttendanceStatus.ABSENT),
                poll.getAttendanceCountByStatus(AttendanceStatus.UNDECIDED)
        );
    }
}