package com.sunghyun.member.adpater.out.persistence.repository;

import com.sunghyun.member.adpater.out.persistence.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpringJpaMemberRepository extends JpaRepository<MemberEntity,Long> {
    Optional<MemberEntity> findMemberById(final String id);
}
