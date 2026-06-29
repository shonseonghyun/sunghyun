package com.sunghyun.member.adpater.out.persistence.repository;

import com.sunghyun.member.adpater.out.persistence.entity.MemberEntity;
import com.sunghyun.member.domain.model.Member;
import com.sunghyun.member.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepository {
    private final SpringJpaMemberRepository springJpaMemberRepository;

    @Override
    public Member save(final Member memberEntity) {
        return springJpaMemberRepository.save(MemberEntity.fromDomain(memberEntity))
                .toDomain();
    }

    @Override
    public Optional<Member> getMemberByMemberNo(final Long memberNo) {
        return springJpaMemberRepository.findById(memberNo)
                .map(MemberEntity::toDomain);
    }

    @Override
    public Member updateMember(final Member memberEntity) {
        return null;
    }

    @Override
    public void delMember(final Long memberNo) {
        springJpaMemberRepository.deleteById(memberNo);
    }

    @Override
    public Optional<Member> getMemberById(final String id) {
        return springJpaMemberRepository.findMemberById(id)
                .map(MemberEntity::toDomain);
    }
}
