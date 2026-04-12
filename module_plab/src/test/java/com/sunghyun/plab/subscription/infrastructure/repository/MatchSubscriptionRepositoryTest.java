package com.sunghyun.plab.subscription.infrastructure.repository;

import com.sunghyun.plab.subscription.domain.enums.ActiveSubType;
import com.sunghyun.plab.subscription.domain.model.MatchSubscription;
import com.sunghyun.plab.subscription.application.port.out.persistence.MatchSubscriptionRepository;
import com.sunghyun.plab.subscription.adapter.out.persistence.MatchSubscriptionRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(MatchSubscriptionRepositoryImpl.class)
class MatchSubscriptionRepositoryTest {

    // 상수 필드 정리
    private static final Long PLAB_MATCH_NO = 1L;
    private static final Long MEMBER_NO = 1L;
    private static final String EMAIL = "sunghyun7895@naver.com";
    private static final Integer TARGET_PLAYER_CNT = 6;
    private static final ActiveSubType SUB_TYPE = ActiveSubType.MANAGER_SUB;

    @Autowired
    private MatchSubscriptionRepository matchSubscriptionRepository;

    @BeforeEach
    void setUp() {
        MatchSubscription matchSubscription = createInTest();
        matchSubscriptionRepository.save(matchSubscription);
    }

    // @AfterEach는 @DataJpaTest의 자동 롤백 기능 덕분에 생략 가능합니다.
//    @AfterEach
//    void clean(){
//        matchSubscriptionRepository.deleteAll();
//    }


    @Test
    @DisplayName("matchSubscriptionRepository 인스턴스는 null이 아니다.")
    void matchSubscriptionRepositoryIsNotNull(){
        assertThat(matchSubscriptionRepository).isNotNull();
    }

//    @Test
//    @DisplayName("구독 정보를 성공적으로 저장한다.")
//    void saveMatchSubscription(){
//        // given
//        MatchSubscription matchSubscription = createInTest();
//
//        // when
//        MatchSubscription savedMatchSubscription = matchSubscriptionRepository.save(matchSubscription);
//
//        // then
//        assertThat(savedMatchSubscription).isNotNull();
//        assertThat(savedMatchSubscription.getPlabMatchNo()).isEqualTo(PLAB_MATCH_NO);
//        assertThat(savedMatchSubscription.getMemberNo()).isEqualTo(MEMBER_NO);
//    }

    @Test
    @DisplayName("회원 번호와 플랩 매치번호로 매치 구독 조회 성공한다.")
    void findMatchSubscriptionByMemberNoAndPlabMatchNo_Success(){
        //when
        Optional<MatchSubscription> optionalMatchSubscription = matchSubscriptionRepository.findMatchSubscriptionByMemberNoAndPlabMatchNo(MEMBER_NO,PLAB_MATCH_NO);

        //then
        assertThat(optionalMatchSubscription).isPresent();
        assertThat(optionalMatchSubscription.get().getMemberNo()).isEqualTo(MEMBER_NO);
        assertThat(optionalMatchSubscription.get().getPlabMatchNo()).isEqualTo(PLAB_MATCH_NO);
    }

    @Test
    @DisplayName("회원 번호와 플랩 매치번호로 매치 구독 조회 성공한다.")
    void findMatchSubscriptionByMemberNoAndPlabMatchNo_Suc2cess(){
        //when
        Optional<MatchSubscription> optionalMatchSubscription = matchSubscriptionRepository.findMatchSubscriptionByMemberNoAndPlabMatchNo(MEMBER_NO,PLAB_MATCH_NO);

        //then
        assertThat(optionalMatchSubscription).isPresent();
        assertThat(optionalMatchSubscription.get().getMemberNo()).isEqualTo(MEMBER_NO);
        assertThat(optionalMatchSubscription.get().getPlabMatchNo()).isEqualTo(PLAB_MATCH_NO);
    }

    private MatchSubscription createInTest(){
        return MatchSubscription.create(
                PLAB_MATCH_NO,
                MEMBER_NO,
                EMAIL,
                TARGET_PLAYER_CNT,
                SUB_TYPE
        );
    }
}