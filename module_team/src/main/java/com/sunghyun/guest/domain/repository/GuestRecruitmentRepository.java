package com.sunghyun.guest.domain.repository;

import com.sunghyun.guest.domain.GuestRecruitment;

import java.util.Optional;

public interface GuestRecruitmentRepository {
    GuestRecruitment save(GuestRecruitment guestRecruitment);
    Optional<GuestRecruitment> findBySchedulePollNo(Long schedulePollNo);
    Optional<GuestRecruitment> findByRecruitmentNo(Long recruitmentNo);
    boolean existsConflictingApplication(Long memberNo, String playDt, String playTm);
}
