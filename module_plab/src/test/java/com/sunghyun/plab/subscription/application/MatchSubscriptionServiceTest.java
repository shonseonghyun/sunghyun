package com.sunghyun.plab.subscription.application;

import com.sunghyun.plab.match.domain.exception.InvalidPlabMatchException;
import com.sunghyun.plab.subscription.application.port.in.dto.MatchSubscriptionModReqDto;
import com.sunghyun.plab.subscription.application.port.in.dto.MatchSubscriptionRegReqDto;
import com.sunghyun.plab.subscription.application.port.out.dto.MatchSubscriptionModResDto;
import com.sunghyun.plab.subscription.application.port.out.dto.MatchSubscriptionRegResDto;
import com.sunghyun.plab.subscription.application.port.out.dto.PlabMatchResDto;
import com.sunghyun.plab.subscription.application.port.out.external.PlabMatchOutPort;
import com.sunghyun.plab.subscription.application.port.out.persistence.MatchSubscriptionRepository;
import com.sunghyun.plab.subscription.domain.enums.ActiveSubType;
import com.sunghyun.plab.subscription.domain.enums.NotiSetting;
import com.sunghyun.plab.subscription.domain.enums.NotiType;
import com.sunghyun.plab.subscription.domain.exception.ExistMatchSubscriptionException;
import com.sunghyun.plab.subscription.domain.exception.NotExistMatchSubscriptionException;
import com.sunghyun.plab.subscription.domain.model.MatchSubscription;
import com.sunghyun.plab.subscription.domain.service.MatchSubscriptionDomainService;
import com.sunghyun.web.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MatchSubscriptionServiceTest {

    @InjectMocks
    private MatchSubscriptionService target;

    @Mock
    private MatchSubscriptionRepository matchSubscriptionRepository;

    @Mock
    private PlabMatchOutPort plabMatchOutPort;

    @Mock
    private MatchSubscriptionDomainService matchSubscriptionDomainService;

    // 상수 필드 정리
    private static final Long PLAB_MATCH_NO = 1L;
    private static final Long MEMBER_NO = 1L;
    private static final String EMAIL = "sunghyun7895@naver.com";
    private static final Integer TARGET_PLAYER_CNT = 6;
    private static final Integer MOD_TARGET_PLAYER_CNT = 8;
    private static final ActiveSubType SUB_TYPE = ActiveSubType.MANAGER_SUB;
    private static final ActiveSubType MOD_SUB_TYPE = ActiveSubType.ALL;
    private static final String STADIUM_NAME = "스타디움명";
    private static final NotiType NOTI_TYPE=NotiType.PLAYER_COUNT;
    private static final NotiSetting NOTI_VALUE= NotiSetting.PLAYER_EIGHT;

    @Test
    @DisplayName("특정 매치 구독 중 플랩 매치 등록 과정에서 InvalidPlabMatchException 예외 발생 시, 구독 프로세스가 중단되고 InvalidPlabMatchException 예외를 던진다.")
    void registerMatchSubscription_Fail_WhenplabMatchOutPortThrowsNotExistPlabMatchException() {
        //given
        final MatchSubscriptionRegReqDto dto = createRegReqDto();

        doThrow(new InvalidPlabMatchException(ErrorCode.P02))
                .when(plabMatchOutPort).registerPlabMatch(anyLong());

        //when,then
        assertThatThrownBy(() -> target.registerMatchSubscription(dto))
                .isInstanceOf(InvalidPlabMatchException.class);
        verify(matchSubscriptionDomainService, never()).createMatchSubscription(anyLong(), anyLong(), anyString(), any(), any());        //then
        verify(plabMatchOutPort, times(1)).registerPlabMatch(PLAB_MATCH_NO);
    }

    @Test
    @DisplayName("특정 매치 구독 중 플랩 매치 등록 과정에서 ExistMatchSubscriptionException 예외 발생 시, 구독 프로세스가 중단되고 ExistMatchSubscriptionException 예외를 던진다.")
    void registerMatchSubscription_Fail_WhenplabMatchOutPortThrowsExistMatchSubscriptionException() {
        //given
        final MatchSubscriptionRegReqDto dto = createRegReqDto();
        PlabMatchResDto plabMatchResDto = PlabMatchResDto.builder().stadiumName(STADIUM_NAME).build();

        doReturn(plabMatchResDto).when(plabMatchOutPort)
                .registerPlabMatch(anyLong());
        doThrow(new ExistMatchSubscriptionException(ErrorCode.P02))
                .when(matchSubscriptionDomainService).createMatchSubscription(anyLong(), anyLong(), anyString(), any(), any());

        //when,then
        assertThatThrownBy(() -> target.registerMatchSubscription(dto))
                .isInstanceOf(ExistMatchSubscriptionException.class);
        verify(matchSubscriptionDomainService, times(1)).createMatchSubscription(anyLong(), anyLong(), anyString(), any(), any());        //then
        verify(plabMatchOutPort, times(1)).registerPlabMatch(PLAB_MATCH_NO);
    }

    @Test
    @DisplayName("플랩 매치 등록 성공한다.")
    void registerMatchSubscription_Success() {
        //given
        final MatchSubscriptionRegReqDto dto = createRegReqDto();
        MatchSubscription matchSubscription = createDomain();

        PlabMatchResDto plabMatchResDto = PlabMatchResDto.builder().stadiumName(STADIUM_NAME).build();

        doReturn(plabMatchResDto).when(plabMatchOutPort)
                .registerPlabMatch(anyLong());
        doReturn(matchSubscription)
                .when(matchSubscriptionDomainService).createMatchSubscription(anyLong(), anyLong(), anyString(), any(), any());
        doReturn(matchSubscription)
                .when(matchSubscriptionRepository).save(any());

        //when
        MatchSubscriptionRegResDto result = target.registerMatchSubscription(dto);

        //then
        verify(matchSubscriptionDomainService, times(1)).createMatchSubscription(anyLong(), anyLong(), anyString(), any(), any());        //then
        verify(plabMatchOutPort, times(1)).registerPlabMatch(PLAB_MATCH_NO);
        assertThat(result).isNotNull();
        assertThat(result.getPlabMatchNo()).isEqualTo(PLAB_MATCH_NO);
        assertThat(result.getPlabMatchResDto().getStadiumName()).isEqualTo(STADIUM_NAME);
        assertThat(result.getNotiValue()).isEqualTo(NOTI_VALUE);
    }

    @Test
    @DisplayName("구독 매치 수정 시 존재하지 않는 매치번호 인입되어 NotExistMatchSubscriptionException 예외 던진다")
    void modifyMatchSubscription_Fail_WhenNotFound() {
        //given
        final Long notExistSubscriptionNo = 1L;
        final MatchSubscriptionModReqDto dto = createModReqDto();

        doReturn(Optional.empty())
                .when(matchSubscriptionRepository).getMatchSubscriptionBySubscriptionNo(notExistSubscriptionNo)
        ;

        //when,then
        assertThatThrownBy(() -> target.modifyMatchSubscription(notExistSubscriptionNo, dto))
                .isInstanceOf(NotExistMatchSubscriptionException.class)
        ;
    }

//    @Test
//    @DisplayName("구독 매치 수정 시 변경 사항 없는 경우 알림 플래그 값 유지되고, 저장 이루어지지 않는다")
//    void modifyMatchSubscription_NoChange() {
//        //given
//        final Long subscriptionNo = 1L;
//        final MatchSubscriptionModReqDto reqDto = createModReqDto();
//        MatchSubscription selectedMatchSubscription = createModReqDto().toDomain(NOTI_TYPE); //변경 사항 없도록 도메인 생성
//
//        doReturn(Optional.of(selectedMatchSubscription))
//                .when(matchSubscriptionRepository).getMatchSubscriptionBySubscriptionNo(subscriptionNo)
//        ;
//        //when
//        MatchSubscriptionModResDto result = target.modifyMatchSubscription(subscriptionNo,reqDto);
//
//        //then
//        verify(matchSubscriptionRepository,never()).save(any());
//        assertThat(result.getSubType()).isEqualTo(MOD_SUB_TYPE);
//        assertThat(result.getTargetPlayerCnt()).isEqualTo(MOD_TARGET_PLAYER_CNT);
//    }

    @Test
    @DisplayName("구독 매치 수정 성공 시 알림 플래그 false 및 변경 원하는 도메인 필드 변경, 저장, 변환 정상적으로 이루어진다 ")
    void modifyMatchSubscription_Success(){
        //given
        final Long subscriptionNo = 1L;
        final MatchSubscriptionModReqDto reqDto = createModReqDto();
        MatchSubscription selectedMatchSubscription = createDomain();

        doReturn(Optional.of(selectedMatchSubscription))
                .when(matchSubscriptionRepository).getMatchSubscriptionBySubscriptionNo(subscriptionNo)
        ;

        //when
        MatchSubscriptionModResDto result = target.modifyMatchSubscription(subscriptionNo,reqDto);

        //then
        verify(matchSubscriptionRepository,times(1)).save(any());
        assertThat(result.getNotiValue()).isEqualTo(NOTI_VALUE);
    }

    private MatchSubscription createDomain(){
        return MatchSubscription.create(
                PLAB_MATCH_NO,
                MEMBER_NO,
                EMAIL,
                NOTI_TYPE,
                NOTI_VALUE
        );
    }
    private MatchSubscriptionRegReqDto createRegReqDto() {
        return MatchSubscriptionRegReqDto.builder()
                .plabMatchNo(PLAB_MATCH_NO)
                .memberNo(MEMBER_NO)
                .email(EMAIL)
                .notiType(NOTI_TYPE)
                .value(NOTI_VALUE)
                .build();
    }
    private MatchSubscriptionModReqDto createModReqDto(){
        return MatchSubscriptionModReqDto.builder()
                .value(NOTI_VALUE)
                .build()
                ;
    }
}