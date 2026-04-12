package com.sunghyun.member.application;

import com.sunghyun.member.application.dto.req.MemberRegisterReqDto;
import com.sunghyun.member.application.dto.res.MemberResDto;
import com.sunghyun.member.domain.enums.Gender;
import com.sunghyun.member.domain.handler.MemberIdPendingHandler;
import com.sunghyun.member.domain.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
public class MemberServiceEventListenerIntegrationTest {
    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @MockBean
    private MemberIdPendingHandler memberIdPendingHandler;

    @Value("${member.valid-id.prefix}")
    private String pendingIdPrefix;

    @Value("${member.valid-id.timeout}")
    private Long timeout;

    // --- 테스트용 공통 상수 정의 ---
    private static final Long MEMBER_ID = 1L;
    private static final String ID = "testId";
    private static final String PWD = "password1234";
    private static final String EMAIL = "test@naver.com";
    private static final String NAME = "홍길동";
    private static final String TEL = "01012345678";
    private static final String BIRTH_DT = "950204";
    private static final Gender GENDER = Gender.MAN;
    private static final String PENDING_TOKEN = "unique";


    @Test
    @DisplayName("이벤트 리스너 내부 예외 발생하여도 회원가입은 성공되어야 한다.")
    void registerSuccessEvenIfListenerFails(){
        //given
        String uniqueId = "failTestId";
        MemberRegisterReqDto memberRegisterReqDto = createMemberRegisterReqDto(uniqueId);
        when(memberIdPendingHandler.getPendingValue(anyString()))
                .thenReturn(PENDING_TOKEN);
        doThrow(new RuntimeException("Redis 연결 실패!!!"))
                .when(memberIdPendingHandler).deletePendingId(anyString());
//        doThrow(new RuntimeException("Redis 연결 실패!!!"))
//                .when(eventPublisher).publishEvent(new MemberRegisteredEvent(ID));

        //when
        MemberResDto memberResDto = memberService.registerMember(memberRegisterReqDto);

        //then
        assertThat(memberResDto.getMemberNo()).isNotNull();
        assertThat(memberResDto.getId()).isEqualTo(uniqueId);

        // 메인 트랜잭션이 성공했으므로 DB에 데이터가 있어야 함
        assertThat(memberRepository.isExistMemberById(uniqueId)).isEqualTo(true);
        // 리스너가 예외를 뱉는 deletePendingId를 실제로 호출했는지 검증
        verify(memberIdPendingHandler,times(1)).deletePendingId(anyString());
//        verify(eventPublisher,times(1)).publishEvent(new MemberRegisteredEvent(ID));
    }

    @Test
    @DisplayName("이벤트 리스너 정상 처리 시, 회원가입 성공된다.")
    void registerSuccessWithListener(){
        //given
        String uniqueId = "successTestId";
        MemberRegisterReqDto memberRegisterReqDto = createMemberRegisterReqDto(uniqueId);
        when(memberIdPendingHandler.getPendingValue(anyString()))
                .thenReturn(PENDING_TOKEN);
        doNothing().when(memberIdPendingHandler).deletePendingId(anyString());

        //when
        memberService.registerMember(memberRegisterReqDto);

        //then
        assertThat(memberRepository.isExistMemberById(uniqueId)).isEqualTo(true);
        verify(memberIdPendingHandler,times(1)).deletePendingId(anyString());
    }

    private MemberRegisterReqDto createMemberRegisterReqDto(final String id){
        return MemberRegisterReqDto.builder()
                .id(id)
                .pwd(PWD)
                .email(EMAIL)
                .name(NAME)
                .tel(TEL)
                .birthDt(BIRTH_DT)
                .gender(GENDER)
                .pendingToken(PENDING_TOKEN)
                .build();
    }
}
