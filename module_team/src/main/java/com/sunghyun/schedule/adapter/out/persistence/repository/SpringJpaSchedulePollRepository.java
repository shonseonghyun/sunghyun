package com.sunghyun.schedule.adapter.out.persistence.repository;

import com.sunghyun.schedule.adapter.out.persistence.entity.SchedulePollEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SpringJpaSchedulePollRepository extends JpaRepository<SchedulePollEntity, Long> {

    // 🟢 Fetch Join 적용 + 필드명(schedulePollNo) 수정 + DISTINCT(중복 제거)
    @Query("SELECT DISTINCT s FROM SchedulePollEntity s " +
            "LEFT JOIN FETCH s.attendances " +
            "WHERE s.teamNo = :teamNo " +
            "AND (:lastSchedulePollNo IS NULL OR s.schedulePollNo < :lastSchedulePollNo) " +
            "ORDER BY s.schedulePollNo DESC")
    List<SchedulePollEntity> findByTeamNoNoOffset(
            @Param("teamNo") Long teamNo,
            @Param("lastSchedulePollNo") Long lastSchedulePollNo,
            Pageable pageable
    );

    // 🟢 단건 조회 시에도 Fetch Join 적용
    @Query("SELECT DISTINCT s FROM SchedulePollEntity s " +
            "LEFT JOIN FETCH s.attendances " +
            "WHERE s.schedulePollNo = :schedulePollNo " +
            "AND s.teamNo = :teamNo")
    Optional<SchedulePollEntity> findByTeamNoAndScheduleNo(
            @Param("schedulePollNo") Long schedulePollNo,
            @Param("teamNo") Long teamNo
    );

    // 🟢 팀 전체 일정 목록 조회 (Fetch Join으로 참석자 정보 함께 로딩)
    @Query("SELECT DISTINCT s FROM SchedulePollEntity s " +
            "LEFT JOIN FETCH s.attendances " +
            "WHERE s.teamNo = :teamNo " +
            "ORDER BY s.schedulePollNo DESC")
    List<SchedulePollEntity> findAllByTeamNo(@Param("teamNo") Long teamNo);

    @Query("""
        SELECT COUNT(s) > 0
        FROM SchedulePollEntity s
        JOIN s.attendances a
        WHERE a.teamMemberNo = :teamMemberNo
          AND a.attendanceStatus = com.sunghyun.schedule.domain.enums.AttendanceStatus.ATTENDING
          AND s.matchInfo.playDt = :playDt
          AND s.matchInfo.playTm = :playTm
    """)
    boolean existsConflictingSchedule(
            @Param("teamMemberNo") Long teamMemberNo,
            @Param("playDt") String playDt,
            @Param("playTm") String playTm
    );
}