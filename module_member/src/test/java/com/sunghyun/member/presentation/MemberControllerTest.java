package com.sunghyun.member.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sunghyun.member.application.MemberService;
import com.sunghyun.member.application.dto.req.MemberModifyReqDto;
import com.sunghyun.member.application.dto.req.MemberRegisterReqDto;
import com.sunghyun.member.application.dto.res.MemberResDto;
import com.sunghyun.member.domain.exception.NotExistMemberNoException;
import com.sunghyun.member.domain.enums.Gender;
import com.sunghyun.web.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(MemberController.class)
class MemberControllerTest {

    @MockBean
    private MemberService memberService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper om;

    // --- 테스트용 공통 상수 정의 ---
    private static final Long MEMBER_NO = 1L;
    private static final String ID = "testId";
    private static final String PWD = "password1234";
    private static final String WRONG_PWD = " ";
    private static final String EMAIL = "test@naver.com";
    private static final String WRONG_EMAIL = "test@";
    private static final String NAME = "홍길동";
    private static final String TEL = "01012345678";
    private static final String BIRTH_DT = "950204";
    private static final Gender GENDER = Gender.MAN;
    private static final String PENDING_TOKEN = "unique";


    @Test
    void mockMvc는null아님(){
        assertThat(mockMvc).isNotNull();
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

    @Test
    void registerMemberWithWrongParameterAndFail() throws Exception {
        MemberRegisterReqDto wrongRequest = createWrongMemberRegisterReqDto();

        mockMvc.perform(post("/member/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(wrongRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ErrorCode.F00.name()))
                .andExpect(jsonPath("$.message").value(ErrorCode.F00.getMessage()))
                // email 필드 에러가 포함되어 있는지 확인
                .andExpect(jsonPath("$.detailMessages[?(@.field == 'email')].reason")
                        .value(hasItem("이메일 형식이 올바르지 않습니다.")))
                // 4. data 필드가 null일 때 JSON에 포함되지 않았는지 확인
                .andExpect(jsonPath("$.data").doesNotExist())
                ;
    }

    @Test
    void registerMemberAndSuccess() throws Exception {
        //given
        MemberRegisterReqDto memberRegisterReqDto = createMemberRegisterReqDto();
        MemberResDto expectedResponse = new MemberResDto(
                MEMBER_NO, ID, PWD, EMAIL, NAME, TEL, BIRTH_DT, GENDER
        );

        given(memberService.registerMember(any(MemberRegisterReqDto.class)))
                .willReturn(expectedResponse);

        mockMvc.perform(post("/member/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(memberRegisterReqDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ErrorCode.S00.name()))
                .andExpect(jsonPath("$.message").value(ErrorCode.S00.getMessage()))
                .andExpect(jsonPath("$.data.memberNo").value(1L))
                .andExpect(jsonPath("$.data.id").value("testId"))
        ;
    }

    @DisplayName("존재하지 않는 회원 번호 조회 시 M00 에러 응답을 반환하고, GlobalExceptionHandler에서 잡혀 에러 응답한다.")
    @Test
    void getMemberByMemberNo_ShouldReturnM00Error() throws Exception {
        // given
        final Long nonExistMemberNo = 999L;

        // Service에서 NotExistMemberNoException을 던지도록 stubbing
        given(memberService.getMemberByMemberNo(nonExistMemberNo))
                .willThrow(new NotExistMemberNoException(ErrorCode.M00));

        // when & then
        mockMvc.perform(get("/member/" + nonExistMemberNo) // GET 요청 가정
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()) // ExceptionHandler의 설정에 따라 (보통 400)

                // GlobalResponse 규격 검증
                .andExpect(jsonPath("$.code").value(ErrorCode.M00.name()))
                .andExpect(jsonPath("$.message").value(ErrorCode.M00.getMessage()))
                .andExpect(jsonPath("$.data").doesNotExist()); // 에러 시 data는 null이므로 제거됨
    }

    @Test
    void getMemberByMemberNo_Success() throws Exception {
        // given
        final Long nonExistMemberNo = 999L;

        // Service에서 NotExistMemberNoException을 던지도록 stubbing
        given(memberService.getMemberByMemberNo(nonExistMemberNo))
                .willThrow(new NotExistMemberNoException(ErrorCode.M00));

        // when & then
        mockMvc.perform(get("/member/" + nonExistMemberNo) // GET 요청 가정
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()) // ExceptionHandler의 설정에 따라 (보통 400)

                // GlobalResponse 규격 검증
                .andExpect(jsonPath("$.code").value(ErrorCode.M00.name()))
                .andExpect(jsonPath("$.message").value(ErrorCode.M00.getMessage()))
                .andExpect(jsonPath("$.data").doesNotExist()); // 에러 시 data는 null이므로 제거됨
    }

    @Test
    @DisplayName("아이디 중복 검사 시 올바르지 않은 파라미터 검증 실패 시 BadRequest 응답한다")
    void validMemberIdWithWrongParameter_fail() throws Exception {
        // given
        final String blankId = " ";

        // when & then
        mockMvc.perform(get("/valid-id/" + blankId) // GET 요청 가정
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()) // ExceptionHandler의 설정에 따라 (보통 400)
                .andExpect(jsonPath("$.code").value(ErrorCode.COMMON_404.name()))
                .andExpect(jsonPath("$.message").value(ErrorCode.COMMON_404.getMessage()))
                ;
    }

    @Test
    @DisplayName("회원 정보 수정 시 memberNo가 누락되면 400 에러와 필수값 미입력 사유를 반환한다")
    void modifyMember_MemberNoIsNull_ReturnsBadRequestWithReason() throws Exception {
        //given
        MemberModifyReqDto memberModifyReqDtoWithNull = MemberModifyReqDto.builder()
                .build();

        //when,then
        mockMvc.perform(put("/member")
                .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(memberModifyReqDtoWithNull)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ErrorCode.F00.name()))
                .andExpect(jsonPath("$.message").value(ErrorCode.F00.getMessage()))
                .andExpect(jsonPath("$.detailMessages[0].field").value("memberNo"))
                .andExpect(jsonPath("$.detailMessages[0].reason").value("필수 입력 값입니다."))
                ;
    }

    @Test
    @DisplayName("회원 정보 수정 시 http status 200과 응답객체를 반환한다.")
    void modifyMember_success() throws Exception {
        //given
        MemberModifyReqDto memberModifyReqDto = MemberModifyReqDto.builder()
                .memberNo(MEMBER_NO)
                .tel(TEL)
                .build();
        MemberResDto memberResDto = MemberResDto.builder()
                        .memberNo(MEMBER_NO)
                        .tel(TEL)
                        .build();

        when(memberService.modifyMember(any()))
                .thenReturn(memberResDto)
                ;

        //when,then
        mockMvc.perform(put("/member")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(memberModifyReqDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ErrorCode.S00.name()))
                .andExpect(jsonPath("$.message").value(ErrorCode.S00.getMessage()))
                .andExpect(jsonPath("$.data.memberNo").value(MEMBER_NO))
                .andExpect(jsonPath("$.data.tel").value(TEL))
        ;
    }
}