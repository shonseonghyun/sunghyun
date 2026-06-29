package com.sunghyun.member.domain.repository;

import com.sunghyun.member.domain.model.Member;

import java.util.Optional;

public interface MemberRepository {
    Member save(final Member member);
    Optional<Member> getMemberByMemberNo(final Long memberNo);
    Member updateMember(final Member member);
    void delMember(final Long memberNo);
    Optional<Member> getMemberById(final String id);
}
