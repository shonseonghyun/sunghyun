package com.sunghyun.member.domain.repository;

import com.sunghyun.member.domain.model.Member;

import java.util.Optional;

public interface MemberRepository {
    Optional<Member> getMemberByMemberNo(final Long memberNo);
    Optional<Member> getMemberById(final String id);
    Member updateMember(final Member member);
    void deleteMember(final Long memberNo);
    Member save(final Member member);
}
