package com.sunghyun.member.application;

import com.sunghyun.config.JwtProvider;
import com.sunghyun.dto.TokenReqDto;
import com.sunghyun.dto.TokenResDto;
import com.sunghyun.member.application.dto.req.MemberLoginResDto;
import com.sunghyun.member.domain.exception.MemberException;
import com.sunghyun.member.domain.model.Member;
import com.sunghyun.member.domain.model.MemberRole;
import com.sunghyun.member.domain.repository.MemberRepository;
import com.sunghyun.web.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthService {
    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;

    public MemberLoginResDto login(final String id,final String inputPwd){
        // 회원 조회
        Member member = memberRepository.getMember(id)
                .orElseThrow(()->new MemberException(ErrorCode.M07));

        // 비밀번호 검증
        if(!member.getPwd().equals(inputPwd)){
            log.error("비밀번호가 일치하지 않습니다.");
            throw new MemberException(ErrorCode.M05);
        }

        // 토큰 발행
        TokenResDto tokenResDto = jwtProvider.generateToken(
                TokenReqDto.builder()
                        .id(id)
                        .pwd(inputPwd)
                        .roles(
                                member.getRoles().stream().map(MemberRole::getRole).toList()
                        )
                        .build()
        );

        return MemberLoginResDto.builder()
                .memberNo(member.getMemberNo())
                .id(id)
                .name(member.getName())
                .tokens(tokenResDto)
                .build()
                ;
    }

}
