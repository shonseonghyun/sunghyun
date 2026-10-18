package com.sunghyun.guest.adapter.out.persistence.repository;

import com.sunghyun.guest.adapter.out.persistence.entity.GuestRecruitmentEntity;
import com.sunghyun.guest.domain.GuestRecruitment;
import com.sunghyun.guest.domain.repository.GuestRecruitmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class GuestRecruitmentRepositoryImpl implements GuestRecruitmentRepository {
    private final SpringJpaGuestRecruitmentRepository springJpaGuestRecruitmentRepository;

    @Override
    public GuestRecruitment save(GuestRecruitment guestRecruitment) {
        return springJpaGuestRecruitmentRepository.save(GuestRecruitmentEntity.fromDomain(guestRecruitment))
                .toDomain();
    }

    @Override
    public Optional<GuestRecruitment> findBySchedulePollNo(Long schedulePollNo) {
        return springJpaGuestRecruitmentRepository.findBySchedulePollNo(schedulePollNo)
                .map(GuestRecruitmentEntity::toDomain);
    }

    @Override
    public Optional<GuestRecruitment> findByRecruitmentNo(Long recruitmentNo) {
        return springJpaGuestRecruitmentRepository.findById(recruitmentNo)
                .map(GuestRecruitmentEntity::toDomain);
    }

    @Override
    public boolean existsConflictingApplication(Long memberNo, String playDt, String playTm) {
        return springJpaGuestRecruitmentRepository.existsConflictingApplication(memberNo, playDt, playTm);
    }
}
