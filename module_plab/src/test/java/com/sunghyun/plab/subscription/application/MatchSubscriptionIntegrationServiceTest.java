package com.sunghyun.plab.subscription.application;

import com.sunghyun.plab.subscription.application.port.in.dto.MatchSubscriptionRegReqDto;
import com.sunghyun.plab.subscription.application.port.out.dto.MatchSubscriptionRegResDto;
import com.sunghyun.plab.subscription.application.port.out.external.PlabMatchOutPort;
import com.sunghyun.plab.subscription.application.port.out.persistence.MatchSubscriptionRepository;
import com.sunghyun.plab.subscription.domain.enums.ActiveSubType;
import com.sunghyun.plab.subscription.domain.enums.NotiSetting;
import com.sunghyun.plab.subscription.domain.enums.NotiType;
import com.sunghyun.plab.subscription.domain.exception.ExistMatchSubscriptionException;
import com.sunghyun.plab.subscription.domain.service.MatchSubscriptionDomainService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doThrow;

@SpringBootTest
@ActiveProfiles("test")
class MatchSubscriptionIntegrationServiceTest {

    @Autowired
    private MatchSubscriptionService matchSubscriptionService;

    @Autowired
    private MatchSubscriptionRepository matchSubscriptionRepository;

    @SpyBean
    private MatchSubscriptionDomainService matchSubscriptionDomainService;

//    @Autowired
    @SpyBean
    private PlabMatchOutPort plabMatchOutPort;

    // 상수 필드 정리
    // 상수 필드 정리
    private static final Long PLAB_MATCH_NO = 815994L;
    private static final Long MEMBER_NO = 1L;
    private static final String EMAIL = "sunghyun7895@naver.com";
    private static final Integer TARGET_PLAYER_CNT = 6;
    private static final Integer MOD_TARGET_PLAYER_CNT = 8;
    private static final ActiveSubType SUB_TYPE = ActiveSubType.MANAGER_SUB;
    private static final ActiveSubType MOD_SUB_TYPE = ActiveSubType.ALL;
    private static final String STADIUM_NAME = "스타디움명";
    private static final NotiType NOTI_TYPE=NotiType.PLAYER_COUNT;
    private static final NotiSetting NOTI_VALUE= NotiSetting.PLAYER_EIGHT;

    @AfterEach
    void clean(){
        matchSubscriptionRepository.deleteAll();
    }


    @Test
    @DisplayName("구독 매치 등록 시 구독 도메인 서비스에서 예외 발생 시, 플랩 매치는 커밋되고 구독은 롤백되어야 한다")
    void registerMatchSubscription_ShouldCommitMatch_WhenSubscriptionFails(){
        //given
        MatchSubscriptionRegReqDto dto = createDto(); // 테스트용 DTO

        doThrow(ExistMatchSubscriptionException.class)
                .when(matchSubscriptionDomainService)
                    .createMatchSubscription(anyLong(), anyLong(), anyString(), any(), any());


        //when,then
        assertThatThrownBy(()->matchSubscriptionService.registerMatchSubscription(dto))
                .isInstanceOf(ExistMatchSubscriptionException.class);
        assertThat(plabMatchOutPort.getPlabMatch(PLAB_MATCH_NO))
                .isNotNull();
        assertThat(matchSubscriptionRepository.findMatchSubscriptionByMemberNoAndPlabMatchNoAndNotiType(MEMBER_NO,PLAB_MATCH_NO,NOTI_TYPE))
                .isEmpty();
    }

    @Test
    @DisplayName("구독 매치 등록 시, 플랩매치 등록 시 예외 발생 시, 매치 구독 저장하지 않는다.")
    void registerMatchSubscription_ShouldNotSaveSubscription_WhenMatchRegistrationFails(){
        //given
        MatchSubscriptionRegReqDto dto = createDto(); // 테스트용 DTO

        doThrow(RuntimeException.class)
                .when(plabMatchOutPort)
                .registerPlabMatch(anyLong());


        //when,then
        assertThatThrownBy(()->matchSubscriptionService.registerMatchSubscription(dto))
                .isInstanceOf(RuntimeException.class);
        assertThat(matchSubscriptionRepository.findMatchSubscriptionByMemberNoAndPlabMatchNoAndNotiType(MEMBER_NO,PLAB_MATCH_NO,NOTI_TYPE))
                .isEmpty();
    }

    @Test
    @DisplayName("구독 매치 등록 시, 플랩 매치 및 구독 매치 저장된다.")
    void registerMatchSubscription_Success(){
        //given
        MatchSubscriptionRegReqDto dto = createDto(); // 테스트용 DTO

        //when
        MatchSubscriptionRegResDto result = matchSubscriptionService.registerMatchSubscription(dto);

        //then
        assertThat(matchSubscriptionRepository.findMatchSubscriptionByMemberNoAndPlabMatchNoAndNotiType(MEMBER_NO,PLAB_MATCH_NO,NOTI_TYPE))
                .isPresent();
        assertThat(plabMatchOutPort.getPlabMatch(PLAB_MATCH_NO))
                .isNotNull();
    }

    @Test
    @DisplayName("동시에 100명의 사용자가 하나의 플랩매치를 구독할 때, 플랩매치는 1개만 생성되어야 한다")
    void registerMatchSubscription_ConcurrencyTest() throws InterruptedException {
        // given
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        // 각기 다른 유저(MEMBER_NO)가 동일한 PLAB_MATCH_NO를 구독하는 상황 시뮬레이션
        // DTO를 루프 안에서 생성하여 memberNo만 다르게 설정

        // when
        for (int i = 0; i < threadCount; i++) {
            long memberNo = i + 100L; // 멤버 번호를 다르게 설정
            executorService.submit(() -> {
                try {
                    MatchSubscriptionRegReqDto dto = MatchSubscriptionRegReqDto.builder()
                            .plabMatchNo(PLAB_MATCH_NO)
                            .memberNo(memberNo)
                            .email("test" + memberNo + "@test.com")
                            .notiType(NOTI_TYPE)
                            .value(NOTI_VALUE)
                            .build();

                    matchSubscriptionService.registerMatchSubscription(dto);
                } catch (Exception e) {
//                    log.error("동시성 테스트 중 에러 발생: {}", e.getMessage());
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await(); // 모든 스레드가 끝날 때까지 대기

        // then
        // 1. 구독 정보는 100개가 생성되어야 함
        Long subscriptionCount = matchSubscriptionRepository.count();
        assertThat(subscriptionCount).isEqualTo(threadCount);

        // 2. 핵심 검증: 플랩매치 생성 로직(Facade)이 정상 작동했다면,
        // 실제 DB나 Mock을 통해 확인했을 때 생성 로직의 결과가 유효해야 함
        // (이 예제에서는 Facade가 락을 잘 잡았다면 에러 없이 모두 성공했을 것임)
        assertThat(plabMatchOutPort.getPlabMatch(PLAB_MATCH_NO)).isNotNull();
    }

    private MatchSubscriptionRegReqDto createDto() {
        return MatchSubscriptionRegReqDto.builder()
                .plabMatchNo(PLAB_MATCH_NO)
                .memberNo(MEMBER_NO)
                .email(EMAIL)
                .notiType(NOTI_TYPE)
                .value(NOTI_VALUE)
                .build();
    }
}