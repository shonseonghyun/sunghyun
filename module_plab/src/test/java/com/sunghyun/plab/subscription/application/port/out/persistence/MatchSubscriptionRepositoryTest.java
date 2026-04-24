package com.sunghyun.plab.subscription.application.port.out.persistence;

import com.sunghyun.plab.subscription.adapter.out.persistence.MatchSubscriptionMapper;
import com.sunghyun.plab.subscription.adapter.out.persistence.MatchSubscriptionRepositoryImpl;
import com.sunghyun.plab.subscription.domain.enums.NotiSetting;
import com.sunghyun.plab.subscription.domain.enums.NotiType;
import com.sunghyun.plab.subscription.domain.model.MatchSubscription;
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
@Import({MatchSubscriptionRepositoryImpl.class,MatchSubscriptionMapper.class})
class MatchSubscriptionRepositoryTest {
    private static final Long NO_EXIST_HISTORY_NO = 99999L;
    private static final Long EXIST_HISTORY_NO = 1L;
    private static final Long PLAB_MATCH_NO = 1L;
    private static final Long MEMBER_NO = 1L;
    private static final String EMAIL = "sunghyun7895@naver.com";
    private static final NotiType NOTI_TYPE=NotiType.PLAYER_COUNT;
    private static final NotiSetting NOTI_VALUE= NotiSetting.PLAYER_TWELVE;

    @Autowired
    private MatchSubscriptionRepository matchSubscriptionRepository;

    @Autowired
    private MatchSubscriptionMapper mapper;

    @BeforeEach
    void setUp() {
        MatchSubscription matchSubscription = create();
        matchSubscriptionRepository.save(matchSubscription);
    }

    @Test
    @DisplayName("matchSubscriptionRepository 인스턴스는 null이 아니다.")
    void matchSubscriptionRepositoryIsNotNull(){
        assertThat(matchSubscriptionRepository).isNotNull();
    }


    @Test
    @DisplayName("저장 성공한다")
    void saveSuccess(){
        //given
        MatchSubscription matchSubscription = create();

        //when
        MatchSubscription savedMatchSubscription = matchSubscriptionRepository.save(matchSubscription);

        //then
        assertThat(savedMatchSubscription).isNotNull();
        assertThat(savedMatchSubscription.getSubscriptionNo()).isNotNull();
        assertThat(savedMatchSubscription.getNotiValue()).isEqualTo(NOTI_VALUE);
    }

    @Test
    @DisplayName("회원 번호와 플랩 매치번호로 매치 구독 조회 성공한다.")
    void findMatchSubscriptionByMemberNoAndPlabMatchNo_Success(){
        //when
        Optional<MatchSubscription> optionalMatchSubscription = matchSubscriptionRepository.findMatchSubscriptionByMemberNoAndPlabMatchNoAndNotiType(MEMBER_NO,PLAB_MATCH_NO,NOTI_TYPE);

        //then
        assertThat(optionalMatchSubscription).isPresent();
        assertThat(optionalMatchSubscription.get().getMemberNo()).isEqualTo(MEMBER_NO);
        assertThat(optionalMatchSubscription.get().getPlabMatchNo()).isEqualTo(PLAB_MATCH_NO);
        assertThat(optionalMatchSubscription.get().getNotiType()).isEqualTo(NOTI_TYPE);
        assertThat(optionalMatchSubscription.get().getNotiValue()).isEqualTo(NOTI_VALUE);

    }

    @Test
    @DisplayName("존재하지 않는 히스토리 번호로 조회 시 Optional.empty 반환된다")
    void findMatchSubscriptionByNoExistHistoryNo(){
        //when
        Optional<MatchSubscription> optionalMatchSubscription = matchSubscriptionRepository.getMatchSubscriptionBySubscriptionNo(NO_EXIST_HISTORY_NO);

        //then
        assertThat(optionalMatchSubscription).isEmpty();
    }

    @Test
    @DisplayName("히스토리 번호로 조회 시 조회된다")
    void findMatchSubscriptionByHistoryNo_Success(){
        //when
        Optional<MatchSubscription> optionalMatchSubscription = matchSubscriptionRepository.getMatchSubscriptionBySubscriptionNo(EXIST_HISTORY_NO);

        //then
        assertThat(optionalMatchSubscription).isPresent();
    }

    private MatchSubscription create(){
        return MatchSubscription.create(
                PLAB_MATCH_NO,
                MEMBER_NO,
                EMAIL,
                NOTI_TYPE,
                NOTI_VALUE
        );
    }

}