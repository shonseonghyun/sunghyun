package com.sunghyun.member.application;

import com.sunghyun.member.application.dto.req.MemberModifyReqDto;
import com.sunghyun.member.application.dto.req.MemberRegisterReqDto;
import com.sunghyun.member.application.dto.res.MemberResDto;
import com.sunghyun.member.application.dto.res.MemberValidIdResDto;
import com.sunghyun.member.application.service.MemberService;
import com.sunghyun.member.domain.enums.Gender;
import com.sunghyun.member.domain.event.MemberRegisteredEvent;
import com.sunghyun.member.domain.exception.*;
import com.sunghyun.member.application.port.repository.MemberIdPendingRepository;
import com.sunghyun.member.adpater.out.persistence.entity.MemberEntity;
import com.sunghyun.member.domain.repository.MemberRepository;
import com.sunghyun.member.domain.service.PasswordService;
import com.sunghyun.web.ErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberEntityServiceTest {

    @InjectMocks
    private MemberService target;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private ApplicationEventPublisher applicationEventPublisher;

    @Mock
    private MemberIdPendingRepository memberIdPendingRepository;

    @Mock
    private PasswordService passwordService;

    // --- 테스트용 공통 상수 정의 ---
    private static final Long MEMBER_NO = 1L;
    private static final String ID = "testId";
    private static final String PWD = "password1234";
    private static final String NEW_PWD = "password12341234";
    private static final String WRONG_PWD = " ";
    private static final String EMAIL = "test@naver.com";
    private static final String NEW_EMAIL = "test2@naver.com";
    private static final String WRONG_EMAIL = "test@";
    private static final String NAME = "홍길동";
    private static final String TEL = "01012345678";
    private static final String BIRTH_DT = "950204";
    private static final Gender GENDER = Gender.MALE;
    private static final String PENDING_TOKEN = "unique";


    // @Value 필드 대용 상수
    private static final String PREFIX = "member:pending:";
    private static final Long TIMEOUT = 5L;

    @BeforeEach
    void setUp() {
        // 단위 테스트에서 @Value 필드는 null이므로 직접 주입해줘야 합니다.
        ReflectionTestUtils.setField(target, "pendingIdPrefix", PREFIX);
        ReflectionTestUtils.setField(target, "timeout", TIMEOUT);
    }

    @Test
    @DisplayName("회원 가입 시, 토큰 존재하지 않아 NotValidatedException 던진다.")
    void test(){
        //given
        MemberRegisterReqDto memberRegisterReqDto = createMemberRegisterReqDto();
        when(memberIdPendingRepository.getPendingValue(anyString()))
                .thenReturn(null)
        ;

        //when,then
        assertThatThrownBy(()->target.registerMember(memberRegisterReqDto))
                .isInstanceOf(NotValidatedIdException.class)
        ;
    }

    @Test
    @DisplayName("회원 가입 시, 토큰 일치하지 않아 InvalidPendingTokenException 던진다.")
    void test2(){
        //given
        final String differentPendingToken = "ad";
        MemberRegisterReqDto memberRegisterReqDto = createMemberRegisterReqDto();
        when(memberIdPendingRepository.getPendingValue(anyString()))
                .thenReturn(differentPendingToken)
        ;

        //when,then
        assertThatThrownBy(()->target.registerMember(memberRegisterReqDto))
                .isInstanceOf(InvalidPendingTokenException.class)
        ;
    }


    @Test
    @DisplayName("회원 가입 성공")
    void registerMemberAndSuccess(){
        //given
        MemberRegisterReqDto memberRegisterReqDto = createMemberRegisterReqDto();

        MemberEntity savedMemberEntity = MemberEntity.builder()
                .memberNo(1L)
                .id(ID)
                .pwd(PWD)
                .email(EMAIL)
                .name(NAME)
                .tel(TEL)
                .birthDt(BIRTH_DT)
                .gender(GENDER)
                .build()
                ;
        when(memberIdPendingRepository.getPendingValue(anyString()))
                .thenReturn(PENDING_TOKEN)
        ;
        when(memberRepository.save(any(MemberEntity.class)))
                .thenReturn(savedMemberEntity)
        ;

        //when
        MemberResDto memberResDto = target.registerMember(memberRegisterReqDto);

        //then
        assertThat(memberResDto.getId()).isEqualTo(ID);
        verify((memberRepository),times(1)).save(any(MemberEntity.class));
        verify(applicationEventPublisher, times(1)).publishEvent(any(MemberRegisteredEvent.class));
    }

    @Test
    void getMemberByMemberNoButThrowNotExistException(){
        //given
        final Long memberNo = 1L;

        when(memberRepository.getMemberByMemberNo(1L))
                .thenReturn(null);

        //when,then
        assertThatThrownBy(()->target.getMemberByMemberNo(memberNo))
                .isInstanceOf(NotExistMemberNoException.class)//예외 타입 검증
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.M000) // 2. 내부 필드(ErrorCode) 값 검증
                .extracting("errorCode.message").isEqualTo(ErrorCode.M000.getMessage()) // 3. (선택사항) 에러코드 내부의 메시지까지 확인하고 싶을 때
        ;
    }

    @Test
    @DisplayName("아아디 중복 검사 시 이미 Redis에 선점된 아이디이기에 PendingIdException 던진다")
    void validMemberId_fail_due_to_redis_pending(){
        //given
        final String uuid = UUID.randomUUID().toString();
        // 핵심: 서비스 객체의 private 필드인 pendingIdPrefix에 직접 값을 주입합니다.
        // @Value 세팅  위해
//        ReflectionTestUtils.setField(target, "pendingIdPrefix", prefix);

        // 핵심: opsForValue() 호출 시 null이 아닌 위에서 선언한 valueOperations 모의 객체를 반환하도록 설정
//        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
//        when(redisTemplate.opsForValue().setIfAbsent(expectedKey,"pending", Duration.ofMinutes(5)))
//                .thenReturn(Boolean.FALSE);

        when(memberIdPendingRepository.lock(anyString(),anyString(),anyLong()))
                .thenReturn(false)
                ;

        //when,then
        assertThatThrownBy(() -> target.validMemberId(ID))
                .isInstanceOf(PendingIdException.class);
        verify(memberIdPendingRepository, never()).unlock(anyString());
    }

    @Test
    @DisplayName("아아디 중복 검사 시 DB 상 이미 존재하는 아이디이기에 AlreadyExistMemberIdException 던진다")
    void validMemberId_fail_due_to_db_pending(){
        // 핵심: 서비스 객체의 private 필드인 pendingIdPrefix에 직접 값을 주입합니다.
        // @Value 세팅  위해
//        ReflectionTestUtils.setField(target, "pendingIdPrefix", prefix);

        // 핵심: opsForValue() 호출 시 null이 아닌 위에서 선언한 valueOperations 모의 객체를 반환하도록 설정
//        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
//        when(redisTemplate.opsForValue().setIfAbsent(expectedKey,"pending", Duration.ofMinutes(5)))
//                .thenReturn(Boolean.TRUE);
        when(memberIdPendingRepository.lock(anyString(),anyString(),anyLong()))
                .thenReturn(true);
        when(memberRepository.isExistMemberById(ID))
                .thenReturn(true);

        //when,then
        assertThatThrownBy(() -> target.validMemberId(ID))
                .isInstanceOf(MemberIdExistException.class);
        verify(memberIdPendingRepository, times(1)).unlock(anyString());
    }

    @Test
    @DisplayName("아아디 중복 검사 시 성공")
    void validMemberId_success(){
        //given
        // 핵심: 서비스 객체의 private 필드인 pendingIdPrefix에 직접 값을 주입합니다.
        // @Value 세팅  위해
//        ReflectionTestUtils.setField(target, "pendingIdPrefix", prefix);

        // 핵심: opsForValue() 호출 시 null이 아닌 위에서 선언한 valueOperations 모의 객체를 반환하도록 설정
//        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
//        when(redisTemplate.opsForValue().setIfAbsent(expectedKey,"pending", Duration.ofMinutes(5)))
//                .thenReturn(Boolean.TRUE);
        when(memberIdPendingRepository.lock(anyString(),anyString(),anyLong()))
                .thenReturn(true);

        when(memberRepository.isExistMemberById(ID))
                .thenReturn(false);

        //when,then
//        assertDoesNotThrow(() -> target.validMemberId(ID));
        final MemberValidIdResDto result = target.validMemberId(ID);
        assertThat(result).isNotNull();
        // DB에 없으므로 Redis 삭제 메서드는 호출되지 않아야 함
        verify(memberIdPendingRepository, never()).unlock(anyString());
    }


    @Test
    @DisplayName("회원 정보 수정 시, 존재하지 않는 멤버 번호 인입되어 NotExistMemberNoException 던진다")
    void modifyMemberButThrowNotExistMemberNoException(){
        //given
        MemberModifyReqDto memberModifyReqDto = MemberModifyReqDto
                .builder()
                .memberNo(MEMBER_NO)
                .email(EMAIL)
                .tel(TEL)
                .currentPwd(PWD)
                .newPwd(PWD+123)
                .gender(Gender.FEMALE)
                .build()
                ;

        when(memberRepository.getMemberByMemberNo(anyLong()))
                .thenReturn(null)
        ;

        //when,then
        assertThatThrownBy(()->target.modifyMember(memberModifyReqDto))
                .isInstanceOf(NotExistMemberNoException.class)
        ;
    }

    @Test
    @DisplayName("회원 정보 수정 시, 조회된 멤버 정보와 수정 요청된 멤버 정보가 같아 저장하지 않고 회원응답 객체 반환한다")
    void modifyMemberSuccess() {
        //given
        MemberModifyReqDto memberModifyReqDto = MemberModifyReqDto
                .builder()
                .memberNo(MEMBER_NO)
                .email(EMAIL)
                .tel(TEL)
                .currentPwd(PWD)
                .newPwd(PWD)
                .gender(Gender.FEMALE)
                .build();

        MemberEntity memberEntity = MemberEntity.builder()
                .memberNo(MEMBER_NO)
                .email(EMAIL)
                .tel(TEL)
                .pwd(PWD)
                .gender(Gender.FEMALE)
                .pwd(PWD)
                .build()
                ;

        when(memberRepository.getMemberByMemberNo(anyLong()))
                .thenReturn(memberEntity)
        ;

        //when
        MemberResDto memberResDto = target.modifyMember(memberModifyReqDto);

        //then
        verify(memberRepository,times(0)).save(any());
    }

    @Test
    @DisplayName("회원 정보 수정 시, 새로운 비밀번호가 요청되어 저장하고 회원응답 객체 반환한다")
    void modifyMemberSuccess3() {
        //given
        MemberModifyReqDto memberModifyReqDto = MemberModifyReqDto
                .builder()
                .memberNo(MEMBER_NO)
                .email(EMAIL)
                .tel(TEL)
                .currentPwd(PWD)
                .newPwd(NEW_PWD)
                .gender(Gender.FEMALE)
                .build()
                ;

        MemberEntity memberEntity = MemberEntity.builder()
                .memberNo(MEMBER_NO)
                .email(EMAIL)
                .tel(TEL)
                .pwd(PWD)
                .gender(Gender.FEMALE)
                .pwd(PWD)
                .build()
                ;


        when(memberRepository.getMemberByMemberNo(anyLong()))
                .thenReturn(memberEntity)
        ;
        when(passwordService.updatePwd(memberModifyReqDto.getCurrentPwd(), memberModifyReqDto.getNewPwd(), memberEntity))
                .thenReturn(true)
        ;

        //when
        MemberResDto memberResDto = target.modifyMember(memberModifyReqDto);

        //then
        verify(memberRepository,times(1)).save(any());
    }

    @Test
    @DisplayName("회원 정보 수정 시, 비밀번호가 아닌 다른 필드 수정 요청되어 저장하고 회원응답 객체 반환한다")
    void modifyMemberSuccess4() {
        //given
        MemberModifyReqDto memberModifyReqDto = MemberModifyReqDto
                .builder()
                .memberNo(MEMBER_NO)
                .email(NEW_EMAIL)
                .tel(TEL)
                .currentPwd(null)
                .newPwd(null)
                .gender(Gender.FEMALE)
                .build()
                ;

        MemberEntity memberEntity = MemberEntity.builder()
                .memberNo(MEMBER_NO)
                .email(EMAIL)
                .tel(TEL)
                .pwd(PWD)
                .gender(Gender.FEMALE)
                .pwd(PWD)
                .build()
                ;


        when(memberRepository.getMemberByMemberNo(anyLong()))
                .thenReturn(memberEntity)
        ;
        when(passwordService.updatePwd(memberModifyReqDto.getCurrentPwd(),memberModifyReqDto.getNewPwd(), memberEntity))
                .thenReturn(true)
        ;

        //when
        MemberResDto memberResDto = target.modifyMember(memberModifyReqDto);

        //then
        verify(memberRepository,times(1)).save(any());
    }

    @Test
    @DisplayName("회원 정보 수정 시, 조회된 멤버 정보와 수정 요청된 멤버 정보가 달라 저장하고 회원응답 객체 반환한다")
    void modifyMemberSuccess2() {
        //given
        MemberModifyReqDto memberModifyReqDto = MemberModifyReqDto
                .builder()
                .memberNo(MEMBER_NO)
                .email(EMAIL)
                .tel(TEL)
                .currentPwd(PWD)
                .newPwd(PWD)
                .gender(Gender.FEMALE)
                .build()
                ;

        MemberEntity memberEntity = MemberEntity.builder()
                .memberNo(MEMBER_NO)
                .email(EMAIL)
                .tel(TEL+123)
                .pwd(PWD)
                .gender(Gender.FEMALE)
                .pwd(PWD)
                .build()
                ;

        when(memberRepository.getMemberByMemberNo(anyLong()))
                .thenReturn(memberEntity)
        ;

        //when
        MemberResDto memberResDto = target.modifyMember(memberModifyReqDto);

        //then
        verify(memberRepository,times(1)).save(any());
    }

    private MemberRegisterReqDto createMemberRegisterReqDto(){
        return MemberRegisterReqDto.builder()
                .id(ID)
                .pwd(PWD)
                .email(EMAIL)
                .name(NAME)
                .tel(TEL)
                .birthDt(BIRTH_DT)
                .gender(GENDER)
                .pendingToken(PENDING_TOKEN)
                .build();
    }

    private MemberRegisterReqDto createWrongMemberRegisterReqDto(){
        return MemberRegisterReqDto.builder()
                .id(ID)
                .pwd(WRONG_PWD)
                .email(WRONG_EMAIL)
                .name(NAME)
                .tel(TEL)
                .birthDt(BIRTH_DT)
                .gender(GENDER)
                .pendingToken(PENDING_TOKEN)
                .build();
    }
}