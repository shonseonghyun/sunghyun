//package com.sunghyun.plab.subscription.adapter.in.web;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.sunghyun.plab.subscription.application.port.in.MatchSubscriptionUseCase;
//import com.sunghyun.plab.subscription.application.port.in.dto.MatchSubscriptionModReqDto;
//import com.sunghyun.plab.subscription.application.port.out.dto.MatchSubscriptionModResDto;
//import com.sunghyun.plab.subscription.domain.enums.ActiveSubType;
//import com.sunghyun.web.ErrorCode;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyLong;
//import static org.mockito.Mockito.doReturn;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@WebMvcTest(MatchSubscriptionController.class)
//class MatchSubscriptionControllerTest {
//
//    @MockBean
//    private MatchSubscriptionUseCase matchSubscriptionUseCase;
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ObjectMapper om;
//
//    private static final Long MATCH_SUBSCRIPTION_NO = 1L;
//    private static final Long PLAB_MATCH_NO = 1L;
//    private static final Long MEMBER_NO = 1L;
//    private static final String EMAIL = "sunghyun7895@naver.com";
//    private static final Integer TARGET_PLAYER_CNT = 6;
//    private static final Integer MOD_TARGET_PLAYER_CNT = 8;
//    private static final ActiveSubType SUB_TYPE = ActiveSubType.MANAGER_SUB;
//    private static final ActiveSubType MOD_SUB_TYPE = ActiveSubType.ALL;
//    private static final String STADIUM_NAME = "스타디움명";
//    private static final String BASE_MODIFY_URL = "/plab/subscription/";
//
//    @Test
//    @DisplayName("구독 매치 수정 시, 구독번호(PathVariable)가 null인 경우 Notfound를 응답한다.")
//    void modifyMatchSubscription_WhenNoPathVariable_Returns404() throws Exception {
//        //when,then
//        mockMvc.perform(put(BASE_MODIFY_URL)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isNotFound())
//                .andExpect(jsonPath("$.code").value(ErrorCode.COMMON_404.name()))
//                .andExpect(jsonPath("$.message").value(ErrorCode.COMMON_404.getMessage()))
//        ;
//    }
//
//    @Test
//    @DisplayName("구독 매치 수정 시, 타겟 인원이 0명인 경우 유효성 검증 실패로 400 에러를 응답한다.")
//    void modifyMatchSubscription_WhenTargetPlayerCntIsZero_ReturnsBadRequest() throws Exception {
//        //given
//        final Long subscriptionNo = 1L;
//        final String url = BASE_MODIFY_URL+subscriptionNo;
//        MatchSubscriptionModReqDto modifyReqDto = new MatchSubscriptionModReqDto(0,null);
//
//        //when,then
//        mockMvc.perform(put(url)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(om.writeValueAsString(modifyReqDto)))
//                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$.code").value(ErrorCode.F00.name()))
//                .andExpect(jsonPath("$.message").value(ErrorCode.F00.getMessage()))
//        ;
//    }
//
//    @Test
//    @DisplayName("구독 매치 수정 시, 요청바디 누락된 경우 HttpMessageNotReadableException 던져 BadRequest 응답한다")
//    void modifyMatchSubscription_WhenRequestBodyMissing_ReturnsBadRequest() throws Exception {
//        //given
//        final Long subscriptionNo = 1L;
//        final String url = BASE_MODIFY_URL+subscriptionNo;
//
//        //when,then
//        mockMvc.perform(put(url)
//                        .contentType(MediaType.APPLICATION_JSON)
//                )
//                .andExpect(status().isBadRequest())
//        ;
//    }
//
//    @Test
//    @DisplayName("구독 매치 수정 시, 숫자가 아닌 PathVariable이 인입되면 400 에러와 상세 메시지를 반환한다.")
//    void modifyMatchSubscription_Fail_WhenInvalidTypePathVariable() throws Exception {
//        //given
//        final String invalidSubscriptionNo = "abc";
//        final String url = BASE_MODIFY_URL+invalidSubscriptionNo;
//        MatchSubscriptionModReqDto modifyReqDto = new MatchSubscriptionModReqDto(TARGET_PLAYER_CNT,null);
//
//        //when,then
//        mockMvc.perform(put(url)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(om.writeValueAsString(modifyReqDto))
//                )
//                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$.detailMessages[0].field").value("subscriptionNo"))
//                .andExpect(jsonPath("$.detailMessages[0].value").value(invalidSubscriptionNo))
//        ;
//    }
//
//    @Test
//    @DisplayName("구독 매치 수정 시, 요청바디(RequestBody)의 요청필드가 null이여도  성공 응답 반환한다")
//    void modifyMatchSubscription_WhenFieldsAreEmpty_ReturnsSuccess() throws Exception {
//        //given
//        final Long subscriptionNo = 1L;
//        final String url = BASE_MODIFY_URL+subscriptionNo;
//        MatchSubscriptionModReqDto modifyReqDto = new MatchSubscriptionModReqDto();
//        MatchSubscriptionModResDto response = new MatchSubscriptionModResDto(MATCH_SUBSCRIPTION_NO,PLAB_MATCH_NO,MEMBER_NO,EMAIL,TARGET_PLAYER_CNT,SUB_TYPE,false); // 필요한 필드 세팅
//
//        // UseCase가 결과값을 반환하도록 Mocking
//        doReturn(response).when(matchSubscriptionUseCase).modifyMatchSubscription(anyLong(), any());
//
//        //when,then
//        mockMvc.perform(put(url)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(om.writeValueAsString(modifyReqDto)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.code").value(ErrorCode.S00.name()))
//                .andExpect(jsonPath("$.message").value(ErrorCode.S00.getMessage()))
//        ;
//    }
//
//
//    @Test
//    @DisplayName("구독 매치 수정 성공 시 정상 응답을 반환한다")
//    void modifyMatchSubscription_Success() throws Exception {
//        // given
//        final Long subscriptionNo = 1L;
//        final String url = BASE_MODIFY_URL+subscriptionNo;
//        MatchSubscriptionModReqDto dto = createModReqDto(MOD_TARGET_PLAYER_CNT, MOD_SUB_TYPE);
//        MatchSubscriptionModResDto response = new MatchSubscriptionModResDto(MATCH_SUBSCRIPTION_NO,PLAB_MATCH_NO,MEMBER_NO,EMAIL,TARGET_PLAYER_CNT,SUB_TYPE,false); // 필요한 필드 세팅
//
//        // UseCase가 결과값을 반환하도록 Mocking
//        doReturn(response).when(matchSubscriptionUseCase).modifyMatchSubscription(anyLong(), any());
//
//        // when & then
//        mockMvc.perform(put(url)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(om.writeValueAsString(dto)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.code").value(ErrorCode.S00.name()));
//    }
//
//    private MatchSubscriptionModReqDto createModReqDto(final Integer targetPlayerCnt,final ActiveSubType subType){
//        return MatchSubscriptionModReqDto.builder()
//                .targetPlayerCnt(targetPlayerCnt)
//                .subType(subType)
//                .build()
//                ;
//    }
//}