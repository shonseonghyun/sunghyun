package com.sunghyun.member.infrastructure.repository;

import com.sunghyun.member.domain.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpringJpaMemberRepository extends JpaRepository<Member,Long> {
    Optional<Member> findMemberById(final String id);
}
