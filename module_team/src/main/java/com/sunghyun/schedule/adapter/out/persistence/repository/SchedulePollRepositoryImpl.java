package com.sunghyun.schedule.adapter.out.persistence.repository;

import com.sunghyun.schedule.adapter.out.persistence.entity.SchedulePollEntity;
import com.sunghyun.schedule.domain.SchedulePoll;
import com.sunghyun.schedule.domain.repository.SchedulePollRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class SchedulePollRepositoryImpl implements SchedulePollRepository {
    private final SpringJpaSchedulePollRepository springJpaSchedulePollRepository;

    @Override
    public SchedulePoll save(SchedulePoll schedulePoll) {
        return springJpaSchedulePollRepository.save(SchedulePollEntity.from(schedulePoll))
                .toDomain();
    }

    @Override
    public List<SchedulePoll> findAllByTeamNoNoOffset(Long teamNo, Long lastSchedulePollNo, int limit) {
        // PageRequest를 사용해 LIMIT 수행
        PageRequest pageRequest = PageRequest.of(0, limit);

        return springJpaSchedulePollRepository.findByTeamNoNoOffset(teamNo, lastSchedulePollNo, pageRequest)
                .stream()
                .map(SchedulePollEntity::toDomain)
                .toList();
    }

    @Override
    public Optional<SchedulePoll> findByTeamNoAndSchedulePollNo(Long teamNo, Long schedulePollNo) {
        return springJpaSchedulePollRepository.findByTeamNoAndScheduleNo(schedulePollNo,teamNo)
                .map(SchedulePollEntity::toDomain)
                ;
    }

    @Override
    public Optional<SchedulePoll> findBySchedulePollNo(Long schedulePollNo) {
        return springJpaSchedulePollRepository.findById(schedulePollNo)
                .map(SchedulePollEntity::toDomain);
    }

    @Override
    public List<SchedulePoll> findAllByTeamNo(Long teamNo) {
        return springJpaSchedulePollRepository.findAllByTeamNo(teamNo)
                .stream()
                .map(SchedulePollEntity::toDomain)
                .toList();
    }

    @Override
    public boolean existsConflictingSchedule(Long teamMemberNo, String playDt, String playTm) {
        return springJpaSchedulePollRepository.existsConflictingSchedule(teamMemberNo, playDt, playTm);
    }
}