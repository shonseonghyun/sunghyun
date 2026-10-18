package com.sunghyun.schedule.adapter.out.persistence.entity;

import com.sunghyun.schedule.domain.PollAttendance;
import com.sunghyun.schedule.domain.enums.AttendanceStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "poll_attendance")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PollAttendanceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pollAttendanceNo;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "schedule_poll_no")
//    private SchedulePollJpaEntity schedulePoll;

    @Column(name = "team_member_no", nullable = false)
    private Long teamMemberNo;

    @Enumerated(EnumType.STRING)
    @Column(name = "attendance_status", nullable = false)
    private AttendanceStatus attendanceStatus;

    @Column(name = "created_dt")
    private String createdDt;

    @Column(name = "created_tm")
    private String createdTm;

    @Column(name = "modified_dt")
    private String modifiedDt;

    @Column(name = "modified_tm")
    private String modifiedTm;

    public PollAttendanceEntity(
            Long pollAttendanceNo,
            Long teamMemberNo,
            AttendanceStatus attendanceStatus,
            String createdDt,
            String createdTm,
            String modifiedDt,
            String modifiedTm
    ) {
        this.pollAttendanceNo = pollAttendanceNo;
        this.teamMemberNo = teamMemberNo;
        this.attendanceStatus = attendanceStatus;
        this.createdDt = createdDt;
        this.createdTm = createdTm;
        this.modifiedDt = modifiedDt;
        this.modifiedTm = modifiedTm;
    }

    // Domain -> JPA Entity
    public static PollAttendanceEntity from(PollAttendance domain) {
        if (domain == null) return null;
        return new PollAttendanceEntity(
                domain.getPollAttendanceNo(),
                domain.getTeamMemberNo(),
                domain.getAttendanceStatus(),
                domain.getCreatedDt(),
                domain.getCreatedTm(),
                domain.getModifiedDt(),
                domain.getModifiedTm()
        );
    }

    // JPA Entity -> Domain
    public PollAttendance toDomain(Long schedulePollNo) {
        return PollAttendance.builder()
                .pollAttendanceNo(this.pollAttendanceNo)
                .teamMemberNo(this.teamMemberNo)
                .attendanceStatus(this.attendanceStatus)
                .createdDt(this.createdDt)
                .createdTm(this.createdTm)
                .modifiedDt(this.modifiedDt)
                .modifiedTm(this.modifiedTm)
                .build();
    }
}