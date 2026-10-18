package com.sunghyun.schedule.domain.repository;

import com.sunghyun.schedule.domain.SchedulePoll;
import java.util.List;
import java.util.Optional;

public interface SchedulePollRepository {
    SchedulePoll save(SchedulePoll schedulePoll);
    // noOffset 조회 추가 (limit 개수만큼 요청)
    List<SchedulePoll> findAllByTeamNoNoOffset(Long teamNo, Long lastSchedulePollNo, int limit);
    Optional<SchedulePoll> findByTeamNoAndSchedulePollNo(Long teamNo, Long schedulePollNo);
    Optional<SchedulePoll> findBySchedulePollNo(Long schedulePollNo);
    List<SchedulePoll> findAllByTeamNo(Long teamNo);
    boolean existsConflictingSchedule(Long teamMemberNo, String playDt, String playTm);
}