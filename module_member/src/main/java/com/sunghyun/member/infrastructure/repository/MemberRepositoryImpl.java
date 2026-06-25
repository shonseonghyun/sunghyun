package com.sunghyun.member.infrastructure.repository;

import com.sunghyun.config.SecurityUserLoader;
import com.sunghyun.dto.SecurityMemberDto;
import com.sunghyun.member.domain.model.Member;
import com.sunghyun.member.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepository, SecurityUserLoader {
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

    @Override
    public Optional<SecurityMemberDto> loadUserById(final String id) {
        return springJpaMemberRepository.findMemberById(id)
                .map(member-> SecurityMemberDto.builder()
                        .memberNo(member.getMemberNo())
                        .id(member.getId())
                        .name(member.getName())
                        .pwd(member.getPwd())
                        .roles(
                                member.getRoles().stream().map(r -> r.getRole().name()).toList()
                        )
                        .build()
                );

    }
}
