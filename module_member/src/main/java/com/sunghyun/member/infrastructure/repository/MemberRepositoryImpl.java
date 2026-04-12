package com.sunghyun.member.infrastructure.repository;

import com.sunghyun.member.domain.model.Member;
import com.sunghyun.member.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepository {
    private final SpringJpaMemberRepository springJpaMemberRepository;

    @Override
    public Member save(final Member member) {
        return springJpaMemberRepository.save(member);
    }

    @Override
    public Member getMemberByMemberNo(final Long memberNo) {
        return springJpaMemberRepository.findById(memberNo)
                .orElse(null);
    }

    @Override
    public Member updateMember(final Member member) {
        return null;
    }

    @Override
    public void delMember(final Long memberNo) {
        springJpaMemberRepository.deleteById(memberNo);
    }

    @Override
    public boolean isExistMemberById(final String id) {
        return springJpaMemberRepository.findMemberById(id).isPresent();
    }
}
