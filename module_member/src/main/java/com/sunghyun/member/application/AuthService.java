package com.sunghyun.member.application;

import com.sunghyun.config.JwtProvider;
import com.sunghyun.config.MemberNotFoundException;
import com.sunghyun.dto.TokenReqDto;
import com.sunghyun.dto.TokenResDto;
import com.sunghyun.member.application.dto.req.MemberLoginResDto;
import com.sunghyun.member.domain.model.Member;
import com.sunghyun.member.domain.model.MemberRole;
import com.sunghyun.member.domain.repository.MemberRepository;
import com.sunghyun.member.domain.service.PasswordService;
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
    private final PasswordService passwordService;

    public MemberLoginResDto login(final String id,final String inputPwd){
        // 회원 조회
        Member member = memberRepository.getMember(id)
                .orElseThrow(()->new MemberNotFoundException(ErrorCode.M007));

        // 비밀번호 검증
        member.checkPassword(inputPwd,passwordService);

        // 토큰 발행
        TokenResDto tokenResDto = jwtProvider.generateToken(
                TokenReqDto.builder()
                        .id(id)
                        .roles(
                                member.getRoles()
                                        .stream()
                                        .map(MemberRole::getRole)
                                        .toList()
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
