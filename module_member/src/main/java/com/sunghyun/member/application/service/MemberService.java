package com.sunghyun.member.application.service;

import com.sunghyun.config.MemberNotFoundException;
import com.sunghyun.member.application.dto.req.MemberModifyReqDto;
import com.sunghyun.member.application.dto.req.MemberRegisterReqDto;
import com.sunghyun.member.application.dto.res.MemberResDto;
import com.sunghyun.member.application.port.repository.MemberIdPendingRepository;
import com.sunghyun.member.application.port.usecase.MemberUseCase;
import com.sunghyun.member.domain.event.MemberRegisteredEvent;
import com.sunghyun.member.domain.exception.NotValidatedIdException;
import com.sunghyun.member.domain.model.Member;
import com.sunghyun.member.domain.repository.MemberRepository;
import com.sunghyun.member.domain.service.PasswordService;
import com.sunghyun.utils.ApiUtils;
import com.sunghyun.web.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService implements MemberUseCase {
    private final MemberRepository memberRepository;
    private final PasswordService passwordService;
    private final MemberIdPendingRepository memberIdPendingRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public MemberResDto getMemberByMemberNo(final Long memberNo){
        //1. 유저 조회
        Member selectedMember = memberRepository.getMemberByMemberNo(memberNo)
                .orElseThrow(()->new MemberNotFoundException(ErrorCode.M000));

        //3. 변환 및 return
        return MemberResDto.from(selectedMember);
    }

    @Override
    @Transactional
    public MemberResDto registerMember(final MemberRegisterReqDto dto){
        final String id = dto.getId();

        //PendingToken 검증
        memberIdPendingRepository.getPendingToken(id)
                .filter(token -> token.equals(dto.getPendingToken()))
                .orElseThrow(()->new NotValidatedIdException(ErrorCode.M003));

        //유저 도메인 생성 및 저장
        Member newMember = dto.toDomain();
        final String encodedPwd = passwordService.encodePwd(dto.getPwd());
        newMember.setPwd(encodedPwd);
        Member savedMember = memberRepository.save(newMember);

        //이벤트 발행
        applicationEventPublisher.publishEvent(new MemberRegisteredEvent(id));

        //유저 데이터 return
        return MemberResDto.from(savedMember);
    }

    @Override
    @Transactional
    public MemberResDto modifyMember(final Long memberNo, final MemberModifyReqDto dto){
        //1. 유저 조회
        Member selectedMember = memberRepository.getMemberByMemberNo(memberNo)
                .orElseThrow(()->new MemberNotFoundException(ErrorCode.M000));

        //업데이트 위해 dto를 도메인으로 매핑
        Member modifyReqMember = dto.toDomain();

        //업데이트 여부 플래그
        boolean isUpdated ;

        //3. 패스워드 업데이트 체킹
        isUpdated = passwordService.updatePwd(dto.getCurrentPwd(),dto.getNewPwd(),selectedMember);

        //4. 그 외 필드 merge
        boolean mergeFlg = ApiUtils.merge(modifyReqMember, selectedMember);
        if(mergeFlg){
            isUpdated = true;
        }

        // 5. 변경사항이 있을 때만 save (JPA라면 Dirty Checking으로 생략 가능)
        if(isUpdated){
            memberRepository.save(selectedMember);
        }

        //6. 변환 및 return
        return MemberResDto.from(selectedMember);
    }

    @Override
    @Transactional
    public void deleteMember(final Long memberNo){
        //1. 유저 조회
        memberRepository.getMemberByMemberNo(memberNo)
                .orElseThrow(()->new MemberNotFoundException(ErrorCode.M000));

        //3. 삭제
        memberRepository.deleteMember(memberNo);
    }
}
