package com.sunghyun.schedule.adapter.out.persistence.entity;

import com.sunghyun.schedule.domain.PollAttendance;
import com.sunghyun.schedule.domain.SchedulePoll;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "schedule_poll")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SchedulePollEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long schedulePollNo;

    @Column(name = "team_no", nullable = false)
    private Long teamNo;

    @Column(name = "title")
    private String title;

    @Embedded
    private MatchInfoEmbeddable matchInfo;

    @Embedded
    private CostInfoEmbeddable costInfo;

    @Embedded
    private PlayerLimitEmbeddable playerLimit;

    @Column(name = "closed_dt")
    private String closedDt;

    @Column(name = "closed_tm")
    private String closedTm;

    @Column(name = "is_closed")
    private boolean isClosed;

    @OneToMany(cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    @JoinColumn(name = "schedule_poll_no")
    private List<PollAttendanceEntity> attendances = new ArrayList<>(); // 4. 기본 초기화

    public SchedulePollEntity(
            Long schedulePollNo,
            Long teamNo,
            String title,
            MatchInfoEmbeddable matchInfo,
            CostInfoEmbeddable costInfo,
            PlayerLimitEmbeddable playerLimit,
            String closedDt,
            String closedTm,
            boolean isClosed,
            List<PollAttendanceEntity> attendances // 파라미터 추가
    ) {
        this.schedulePollNo = schedulePollNo;
        this.teamNo = teamNo;
        this.title = title;
        this.matchInfo = matchInfo;
        this.costInfo = costInfo;
        this.playerLimit = playerLimit;
        this.closedDt = closedDt;
        this.closedTm = closedTm;
        this.isClosed = isClosed;
        this.attendances = attendances != null ? attendances : new ArrayList<>(); // 2. 누락되었던 필드 할당 수정
    }

    // Domain Model -> JPA Entity 변환
    public static SchedulePollEntity from(SchedulePoll domain) {
        if (domain == null) return null;

        List<PollAttendanceEntity> attendanceEntities = domain.getAttendances() != null
                ? domain.getAttendances().stream().map(PollAttendanceEntity::from).toList()
                : new ArrayList<>();

        return new SchedulePollEntity(
                domain.getSchedulePollNo(),
                domain.getTeamNo(),
                domain.getTitle(),
                MatchInfoEmbeddable.from(domain.getMatchInfo()),
                CostInfoEmbeddable.from(domain.getCostInfo()),
                PlayerLimitEmbeddable.from(domain.getPlayerLimit()),
                domain.getClosedDt(),
                domain.getClosedTm(),
                domain.isClosed(),
                attendanceEntities
        );
    }

    // JPA Entity -> Domain Model 변환
    public SchedulePoll toDomain() {
        List<PollAttendance> domainAttendances = this.attendances != null
                ? this.attendances.stream().map(a -> a.toDomain(this.schedulePollNo)).toList()
                : Collections.emptyList();

        return SchedulePoll.builder()
                .schedulePollNo(this.schedulePollNo)
                .teamNo(this.teamNo)
                .title(this.title)
                .matchInfo(this.matchInfo != null ? this.matchInfo.toDomain() : null)
                .costInfo(this.costInfo != null ? this.costInfo.toDomain() : null)
                .playerLimit(this.playerLimit != null ? this.playerLimit.toDomain() : null)
                .closedDt(this.closedDt)
                .closedTm(this.closedTm)
                .isClosed(this.isClosed)
                .attendances(domainAttendances)
                .build();
    }
}