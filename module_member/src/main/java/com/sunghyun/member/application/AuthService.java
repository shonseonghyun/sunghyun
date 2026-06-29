package com.sunghyun.member.application;

import com.sunghyun.config.JwtProvider;
import com.sunghyun.config.MemberNotFoundException;
import com.sunghyun.dto.TokenReqDto;
import com.sunghyun.dto.TokenResDto;
import com.sunghyun.exceptions.InvalidTokenException;
import com.sunghyun.member.application.dto.req.TokenReissueReqDto;
import com.sunghyun.member.application.dto.res.MemberLoginResDto;
import com.sunghyun.member.application.dto.res.MemberValidIdResDto;
import com.sunghyun.member.application.dto.res.TokenReissueResDto;
import com.sunghyun.member.domain.exception.AlreadyExistMemberIdException;
import com.sunghyun.member.domain.exception.PendingIdException;
import com.sunghyun.member.domain.handler.MemberIdPendingHandler;
import com.sunghyun.member.domain.handler.RefreshTokenHandler;
import com.sunghyun.member.domain.model.Member;
import com.sunghyun.member.domain.model.MemberRole;
import com.sunghyun.member.domain.repository.MemberRepository;
import com.sunghyun.member.domain.service.PasswordService;
import com.sunghyun.utils.ApiUtils;
import com.sunghyun.web.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthService {
    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;
    private final PasswordService passwordService;
    private final RefreshTokenHandler refreshTokenHandler;
    private final MemberIdPendingHandler memberIdPendingHandler;

    @Transactional(readOnly = true)
    public MemberValidIdResDto validMemberId(final String id) {
        // 사용자 구별 가능한 토큰 또는 UUID
        final String pendingToken = ApiUtils.getUUID();

        //1. Redis 내 요청 ID 존재 여부 확인
        // 아이디 선점 시도 (Redis 로직 추상화)
        if (!memberIdPendingHandler.lock(id,pendingToken)) {
            throw new PendingIdException(ErrorCode.M001);
        }

        //2. DB 내 요청 ID 존재 여부 확인
        boolean existInDbFlg = memberRepository.isExistMemberById(id);
        if (existInDbFlg) {
            // DB에 이미 있으면 선점했던 키 해제
            memberIdPendingHandler.unlock(id);
            throw new AlreadyExistMemberIdException(ErrorCode.M002);
        }

        //3. 사용가능한 아이디 검증완료
        // 사용자 구별 가능한 토큰 또는 UUID 발급
        return new MemberValidIdResDto(pendingToken);
    }

    // redis는 트랜잭션 어떻게 해야할까?
    public MemberLoginResDto login(final String id,final String inputPwd){
        // 회원 조회
        Member selectedMember = memberRepository.getMemberById(id)
                .orElseThrow(()->new MemberNotFoundException(ErrorCode.M000));

        // 비밀번호 검증
        selectedMember.checkPassword(inputPwd,passwordService);

        // 토큰 발행 및 저장
        TokenResDto tokenResDto = jwtProvider.generateToken(createTokenReqDto(selectedMember));
        refreshTokenHandler.lock(id,tokenResDto.getRefreshToken());

        return MemberLoginResDto.builder()
                .memberNo(selectedMember.getMemberNo())
                .id(id)
                .name(selectedMember.getName())
                .tokens(tokenResDto)
                .build()
                ;
    }

    // redis는 트랜잭션 어떻게 해야할까?
    public TokenReissueResDto reissueAccessToken(TokenReissueReqDto tokenReissueReqDto) throws InvalidTokenException {
        final String id = tokenReissueReqDto.getId();

        // 회원 조회
        Member selectedMember = memberRepository.getMemberById(id)
                .orElseThrow(()->new MemberNotFoundException(ErrorCode.M000));

        // 저장소 내 refreshToken 조회
        Object selectedRefreshToken = refreshTokenHandler.getRefreshToken(id);

        // refresh 토큰 검증
        if(selectedRefreshToken == null){throw new InvalidTokenException(ErrorCode.F000);}
        if(!selectedRefreshToken.equals(tokenReissueReqDto.getRefreshToken())){
            throw new InvalidTokenException(ErrorCode.F000);
        }

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
                .roles(
                        member.getRoles()
                                .stream()
                                .map(MemberRole::getRole)
                                .toList()
                )
                .build()
                ;
    }
}
