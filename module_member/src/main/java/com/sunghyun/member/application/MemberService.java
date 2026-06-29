package com.sunghyun.member.application;

import com.sunghyun.member.application.dto.req.MemberModifyReqDto;
import com.sunghyun.member.application.dto.req.MemberRegisterReqDto;
import com.sunghyun.member.application.dto.res.MemberResDto;
import com.sunghyun.member.domain.event.MemberRegisteredEvent;
import com.sunghyun.member.domain.exception.*;
import com.sunghyun.member.domain.handler.MemberIdPendingHandler;
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
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordService passwordService;
    private final MemberIdPendingHandler memberIdPendingHandler;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Transactional
    public MemberResDto registerMember(final MemberRegisterReqDto dto){
        final String id = dto.getId();

        //토큰 조회
        final Object selectedPendingToken = memberIdPendingHandler.getPendingValue(id);

        //토큰이 존재하지 않는 경우(타임아웃 or 중복체크 미이행)
        if(selectedPendingToken==null){
            throw new NotValidatedIdException(ErrorCode.M003);
        }

        //토큰이 일치하지 않는 경우
        if(!selectedPendingToken.equals(dto.getPendingToken())){
            throw new InvalidPendingTokenException(ErrorCode.M004);
        }

        //유저 도메인 생성 및 저장
        Member newMember = dto.toDomain();
        newMember.encryptPassword(passwordService);
        Member registeredMember = memberRepository.save(newMember);

        //이벤트 발행
        applicationEventPublisher.publishEvent(new MemberRegisteredEvent(id));

        //유저 데이터 return
        return MemberResDto.from(registeredMember);
    }

    @Transactional(readOnly = true)
    public MemberResDto getMemberByMemberNo(final Long memberNo){
        //1. 유저 조회
        Member selectedMember = memberRepository.getMemberByMemberNo(memberNo);

        //2. 유저 없는 경우 예외 발생
        if(selectedMember == null){
            throw new NotExistMemberNoException(ErrorCode.M000);
        }

        //3. 변환 및 return
        return MemberResDto.from(selectedMember);
    }

    @Transactional
    public MemberResDto modifyMember(final MemberModifyReqDto dto){
        //1. 유저 조회
        Member selectedMember = memberRepository.getMemberByMemberNo(dto.getMemberNo());
        //2. 유저 없는 경우 예외 발생
        if(selectedMember == null){
            throw new NotExistMemberNoException(ErrorCode.M000);
        }

        //업데이트 위해 dto를 도메인으로 매핑
        Member modifyReqMember = dto.toDomain();

        //업데이트 여부 플래그
        boolean isUpdated ;

        //3. 패스워드 업데이트 체킹
        isUpdated = passwordService.updatePwd(dto.getCurrentPwd(),dto.getNewPwd(),selectedMember);

        //4. 그 외 필드 merge
        boolean mergeFlg = ApiUtils.merge(modifyReqMember,selectedMember);
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

    @Transactional
    public void deleteMember(final Long memberNo){
        //1. 유저 조회
        Member selectedMember = memberRepository.getMemberByMemberNo(memberNo);

        //2. 유저 없는 경우 예외 발생
        if(selectedMember == null){
            throw new NotExistMemberNoException(ErrorCode.M000);
        }

        //3. 삭제
        memberRepository.delMember(memberNo);
    }
}
