package com.sunghyun.schedule.domain;

import com.sunghyun.schedule.domain.enums.AttendanceStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class PollAttendance {
    private Long pollAttendanceNo;

    // 이건 여기 있어야 할라나 몰겠다.
//    private Long schedulePollNo;

     /*
        둘 중 하나인데 teamMemberNo가 맞다.
        왜냐하면 투표는 '일반회원=realMemberNo'이 아닌 '팀에 속한 멤버'의 행위이다.
     */
//    private Long realMemberNo;
    private Long teamMemberNo;

    private AttendanceStatus attendanceStatus;

    private String createdDt;
    private String createdTm;

    private String modifiedDt;
    private String modifiedTm;

    public static PollAttendance create(Long teamMemberNo) {
        return PollAttendance
                .builder()
                .attendanceStatus(AttendanceStatus.UNDECIDED)
                .teamMemberNo(teamMemberNo)
                .build()
                ;
    }

    public void changeStatus(AttendanceStatus status) {
        this.attendanceStatus=status;
    }
}
