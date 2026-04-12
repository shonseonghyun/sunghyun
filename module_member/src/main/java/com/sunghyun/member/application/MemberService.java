package com.sunghyun.member.application;

import com.sunghyun.member.application.dto.req.MemberModifyReqDto;
import com.sunghyun.member.application.dto.req.MemberRegisterReqDto;
import com.sunghyun.member.application.dto.res.MemberResDto;
import com.sunghyun.member.application.dto.res.MemberValidIdResDto;
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
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${member.valid-id.prefix}")
    private String pendingIdPrefix;

    @Value("${member.valid-id.timeout}")
    private Long timeout;

    @Transactional
    public MemberResDto registerMember(final MemberRegisterReqDto dto){
        //서비스에서 정의한 규칙대로 Key 생성
        final String key = pendingIdPrefix + dto.getId();

        //토큰 조회
        final Object selectedPendingToken = memberIdPendingHandler.getPendingValue(key);

        //토큰이 존재하지 않는 경우(타임아웃 or 중복체크 미이행)
        if(selectedPendingToken==null){
            throw new NotValidatedIdException(ErrorCode.M03);
        }

        //토큰이 일치하지 않는 경우
        if(!selectedPendingToken.equals(dto.getPendingToken())){
            throw new InvalidPendingTokenException(ErrorCode.M04);
        }

        //유저 도메인 생성 및 저장
        Member newMember = dto.toDomain();
        Member registeredMember = memberRepository.save(newMember);

        //이벤트 발행
        //가입 완료 후 Redis 삭제를 위해 (TransactionalEventListener가 처리)
        //근데 이거 비동기로 동작하냐???????????????????????
        applicationEventPublisher.publishEvent(new MemberRegisteredEvent(key));

        //유저 데이터 return
        return MemberResDto.from(registeredMember);
    }

    @Transactional(readOnly = true)
    public MemberResDto getMemberByMemberNo(final Long memberNo){
        //1. 유저 조회
        Member selectedMember = memberRepository.getMemberByMemberNo(memberNo);

        //2. 유저 없는 경우 예외 발생
        if(selectedMember == null){
            throw new NotExistMemberNoException(ErrorCode.M00);
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
            throw new NotExistMemberNoException(ErrorCode.M00);
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
            throw new NotExistMemberNoException(ErrorCode.M00);
        }

        //3. 삭제
        memberRepository.delMember(memberNo);
    }

    //트랜잭션 없어도 되나?
    //트랜잭션은 왜 필요하나? 원자성을 보장하기 위한것이지.
    //해당 메소드 내에선 사실 읽는 것을 제외하곤 save,update가 되지 않기에 필요하지 않다고 느낌.
    //그러면 Transactioanl(readonly=true)는 왜 존재할가?? 읽기만 하는데 왜??
    @Transactional(readOnly = true)
    public MemberValidIdResDto validMemberId(final String id) {
        final String key = pendingIdPrefix + id;
        // 사용자 구별 가능한 토큰 또는 UUID
        final String pendingToken = ApiUtils.getUUID();

        //1. Redis 내 요청 ID 존재 여부 확인
        // 아이디 선점 시도 (Redis 로직 추상화)
        if (!memberIdPendingHandler.lock(key,pendingToken,timeout)) {
            throw new PendingIdException(ErrorCode.M01);
        }

        //2. DB 내 요청 ID 존재 여부 확인
        boolean existInDbFlg = memberRepository.isExistMemberById(id);
        if (existInDbFlg) {
            // DB에 이미 있으면 선점했던 키 해제
            memberIdPendingHandler.unlock(key);
            throw new AlreadyExistMemberIdException(ErrorCode.M02);
        }

        //3. 사용가능한 아이디 검증완료
        // 사용자 구별 가능한 토큰 또는 UUID 발급
        return new MemberValidIdResDto(pendingToken,timeout);
    }
}
