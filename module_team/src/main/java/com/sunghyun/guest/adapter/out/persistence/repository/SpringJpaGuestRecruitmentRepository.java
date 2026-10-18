package com.sunghyun.guest.adapter.out.persistence.repository;

import com.sunghyun.guest.adapter.out.persistence.entity.GuestRecruitmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SpringJpaGuestRecruitmentRepository extends JpaRepository<GuestRecruitmentEntity, Long> {

    @Query("""
        SELECT COUNT(gr) > 0
        FROM GuestRecruitmentEntity gr
        JOIN gr.guestApplications ga
        WHERE ga.memberNo = :memberNo
          AND ga.applicationStatus = 'CONFIRMED'
          AND gr.playDt = :playDt
          AND gr.playTm = :playTm
    """)
    boolean existsConflictingApplication(
            @Param("memberNo") Long memberNo,
            @Param("playDt") String playDt,
            @Param("playTm") String playTm
    );

    Optional<GuestRecruitmentEntity> findBySchedulePollNo(Long schedulePollNo);
}