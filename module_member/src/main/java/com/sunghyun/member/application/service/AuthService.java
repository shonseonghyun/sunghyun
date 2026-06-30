package com.sunghyun.member.application.service;

import com.sunghyun.config.JwtProvider;
import com.sunghyun.config.MemberNotFoundException;
import com.sunghyun.dto.TokenReqDto;
import com.sunghyun.dto.TokenResDto;
import com.sunghyun.exceptions.InvalidTokenException;
import com.sunghyun.member.application.dto.req.TokenReissueReqDto;
import com.sunghyun.member.application.dto.res.MemberLoginResDto;
import com.sunghyun.member.application.dto.res.MemberValidIdResDto;
import com.sunghyun.member.application.dto.res.TokenReissueResDto;
import com.sunghyun.member.application.port.usecase.AuthUseCase;
import com.sunghyun.member.application.port.repository.MemberIdPendingRepository;
import com.sunghyun.member.application.port.repository.RefreshTokenRepository;
import com.sunghyun.member.domain.exception.MemberIdExistException;
import com.sunghyun.member.domain.exception.PasswordMismatchException;
import com.sunghyun.member.domain.exception.PendingIdException;
import com.sunghyun.member.domain.model.Member;
import com.sunghyun.member.domain.repository.MemberRepository;
import com.sunghyun.member.domain.service.PasswordService;
import com.sunghyun.utils.ApiUtils;
import com.sunghyun.web.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthService implements AuthUseCase {
    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;
    private final PasswordService passwordService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final MemberIdPendingRepository memberIdPendingRepository;

    @Override
    public MemberValidIdResDto validMemberId(final String id) {
        // 사용자 구별 가능한 토큰 또는 UUID
        final String pendingIdToken = ApiUtils.getUUID();
        boolean isSucceeded = false ;

        //1. Redis 내 요청 ID 존재 여부 확인
        // 아이디 선점 시도 (Redis 로직 추상화)
        // 동시성 해결
        if (!memberIdPendingRepository.lock(id,pendingIdToken)) {
            throw new PendingIdException(ErrorCode.M001);
        }

        try{
            //2. DB 내 요청 ID 존재 여부 확인
            memberRepository.getMemberById(id)
                    .ifPresent(member -> {
                        throw new MemberIdExistException(ErrorCode.M002);
                    });

            isSucceeded = true;

            // 사용자 구별 가능한 토큰 발급
            return new MemberValidIdResDto(pendingIdToken);
        }finally {
            // Reis 토큰 검증 시 토큰 값 저장 후 예외 발생 시에만, 락을 푼다.
            if(!isSucceeded){
                memberIdPendingRepository.unlock(id);
            }
        }
    }

    @Override
    public MemberLoginResDto login(final String id,final String inputRawPwd){
        // 회원 조회
        Member selectedMember = memberRepository.getMemberById(id)
                .orElseThrow(()->new MemberNotFoundException(ErrorCode.M007));

        // 비밀번호 검증
        final String encodedPwd = selectedMember.getPwd();
        if(!passwordService.checkPwd(inputRawPwd,encodedPwd)){
            throw new PasswordMismatchException(ErrorCode.M007);
        }

        // 토큰 발행 및 저장
        TokenResDto tokenResDto = jwtProvider.generateToken(createTokenReqDto(selectedMember));
        refreshTokenRepository.lock(id,tokenResDto.getRefreshToken());

        return MemberLoginResDto.builder()
                .memberNo(selectedMember.getMemberNo())
                .id(id)
                .name(selectedMember.getName())
                .tokens(tokenResDto)
                .build()
                ;
    }

    @Override
    public TokenReissueResDto reissueAccessToken(final TokenReissueReqDto tokenReissueReqDto) {
        final String id = tokenReissueReqDto.getId();

        // 회원 조회
        Member selectedMember = memberRepository.getMemberById(id)
                .orElseThrow(()->new MemberNotFoundException(ErrorCode.M000));

        // refreshToken 검증
        refreshTokenRepository.getRefreshToken(id)
                .filter(refreshToken->refreshToken.equals(tokenReissueReqDto.getRefreshToken()))
                .orElseThrow(()->new InvalidTokenException(ErrorCode.M009));

        // accessToken 재발행
        TokenResDto tokenResDto = jwtProvider.reissueAccessToken(
                createTokenReqDto(selectedMember),
                tokenReissueReqDto.getRefreshToken()
        );

        return TokenReissueResDto.builder()
                .id(id)
                .tokens(tokenResDto)
                .build()
                ;
    }

    private TokenReqDto createTokenReqDto(Member member){
        return TokenReqDto.builder()
                .id(member.getId())
                .roles(member.getRoles())
                .build()
                ;
    }
}
