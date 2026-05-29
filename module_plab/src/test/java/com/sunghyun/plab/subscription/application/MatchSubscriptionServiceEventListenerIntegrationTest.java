//package com.sunghyun.plab.subscription.application;
//
//import com.sunghyun.notification.application.event.NotificationEventListener;
//import com.sunghyun.notification.application.port.out.NotiHistoryRepository;
//import com.sunghyun.notification.domain.model.NotiHistory;
//import com.sunghyun.plab.subscription.application.port.in.MatchSubscriptionUseCase;
//import com.sunghyun.plab.subscription.application.port.in.dto.MatchSubscriptionRegReqDto;
//import com.sunghyun.plab.subscription.application.port.out.dto.PlabMatchResDto;
//import com.sunghyun.plab.subscription.application.port.out.external.PlabMatchOutPort;
//import com.sunghyun.plab.subscription.application.port.out.persistence.MatchSubscriptionRepository;
//import com.sunghyun.plab.subscription.domain.enums.ActiveSubType;
//import com.sunghyun.plab.subscription.domain.enums.NotiSetting;
//import com.sunghyun.plab.subscription.domain.enums.NotiType;
//import com.sunghyun.plab.subscription.domain.model.MatchSubscription;
//import com.sunghyun.plab.subscription.domain.service.MatchSubscriptionDomainService;
//import jakarta.persistence.EntityManager;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.boot.test.mock.mockito.SpyBean;
//import org.springframework.context.ApplicationEventPublisher;
//import org.springframework.test.context.ActiveProfiles;
//
//import java.time.Duration;
//import java.util.List;
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.awaitility.Awaitility.await;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyLong;
//import static org.mockito.Mockito.*;
//
//@SpringBootTest
//@ActiveProfiles("test")
//class MatchSubscriptionServiceEventListenerIntegrationTest {
//    private static final Long PLAB_MATCH_NO = 857849L;
//    private static final Long MEMBER_NO = 1L;
//    private static final String EMAIL = "sunghyun7895@naver.com";
//    private static final Integer TARGET_PLAYER_CNT = 6;
//    private static final Integer MOD_TARGET_PLAYER_CNT = 8;
//    private static final ActiveSubType SUB_TYPE = ActiveSubType.MANAGER_SUB;
//    private static final String STADIUM_NAME = "스타디움명";
//    private static final NotiType NOTI_TYPE=NotiType.PLAYER_COUNT;
//    private static final NotiSetting NOTI_VALUE= NotiSetting.PLAYER_ELEVEN;
//
//    @Autowired
//    private MatchSubscriptionUseCase matchSubscriptionUseCase;
//
//    @MockBean
//    private PlabMatchOutPort plabMatchOutPort;
//
//    @Autowired
//    private ApplicationEventPublisher applicationEventPublisher;
//
//    @SpyBean
//    private NotificationEventListener notificationEventListener;
//
//    @Autowired
//    private MatchSubscriptionRepository matchSubscriptionRepository;
//
//    @Autowired
//    private NotiHistoryRepository notiHistoryRepository;
//
//    @Autowired
//    private MatchSubscriptionDomainService matchSubscriptionDomainService;
//
//    @Test
//    @DisplayName("매치 구독 성공하고, 알림 발송 검증 내부 실패 시에도 매치 구독 정상등록된다.")
//    void test1(){
//        //given
//        final MatchSubscriptionRegReqDto reqDto = createRegReqDto();
//        // 외부API 사용하지 않도록 MockBean으로 교체
//        when(plabMatchOutPort.registerPlabMatch(anyLong()))
//        .thenReturn(
//            PlabMatchResDto
//            .builder()
//            .plabMatchNo(PLAB_MATCH_NO)
//            .subType(NotiSetting.NONE)
//            .playerCnt(NotiSetting.PLAYER_ELEVEN)
//            .build()
//        );
//        // 이벤트 처리 도중 예외 발생
//        doThrow(new RuntimeException("강제발생"))
//                .when(notificationEventListener).doNoti(any())
//                ;
//
//
//        //when
//        matchSubscriptionUseCase.registerMatchSubscription(reqDto);
//
//        //then
//        //매치 구독 정상등록 확인
//        Optional<MatchSubscription> optionalMatchSubscription = matchSubscriptionRepository.findMatchSubscriptionByMemberNoAndPlabMatchNoAndNotiType(
//                MEMBER_NO,
//                PLAB_MATCH_NO,
//                NOTI_TYPE
//        );
//        assertThat(optionalMatchSubscription).isPresent();
//    }
//
//    @Test
//    @DisplayName("매치 구독 성공하고, 알림 발송 검증 내부 성공하여 알림 발송하고, 매치 구독 정상 등록된다.")
//    void test2(){
//        //given
//        final MatchSubscriptionRegReqDto reqDto = createRegReqDto();
//        when(plabMatchOutPort.registerPlabMatch(anyLong()))
//                .thenReturn(
//                        PlabMatchResDto
//                                .builder()
//                                .plabMatchNo(PLAB_MATCH_NO)
//                                .subType(NotiSetting.NONE)
//                                .playerCnt(NOTI_VALUE)
//                                .build()
//                );
//
//
//        //when
//        matchSubscriptionUseCase.registerMatchSubscription(reqDto);
//
//        //then
//        //매치 구독 정상등록 확인
//        Optional<MatchSubscription> optionalMatchSubscription = matchSubscriptionRepository.findMatchSubscriptionByMemberNoAndPlabMatchNoAndNotiType(
//                MEMBER_NO,
//                PLAB_MATCH_NO,
//                NOTI_TYPE
//        );
//
//        // 1.
//        assertThat(optionalMatchSubscription).isPresent();
//        verify(notificationEventListener,times(1)).doNoti(any());
//
//        // 2. [수정] 200ms마다 DB를 새로 조회하여 size가 1이 되는지 확인 (최대 5초면 충분합니다)
////        await().atMost(Duration.ofSeconds(5)) // 비동기 테스트에서 2분은 너무 길어요! 보통 5초 내외로 잡습니다.
////                .pollInterval(Duration.ofMillis(200))
////                .untilAsserted(() -> {
////                    // 💡 주기적으로 쿼리를 새로 날려 비동기 스레드가 커밋한 데이터를 가져옵니다.
//////                    List<NotiHistory> notiHistoryList = notiHistoryRepository.getNotiHistoriesByMemberNo(MEMBER_NO);
////                    List<NotiHistory> notiHistoryList = notiHistoryRepository.findAll();
////                    assertThat(notiHistoryList).hasSize(1);
////                });
//    }
//
//    private MatchSubscriptionRegReqDto createRegReqDto() {
//        return MatchSubscriptionRegReqDto.builder()
//                .plabMatchNo(PLAB_MATCH_NO)
//                .memberNo(MEMBER_NO)
//                .email(EMAIL)
//                .notiType(NOTI_TYPE)
//                .value(NOTI_VALUE)
//                .build();
//    }
//}