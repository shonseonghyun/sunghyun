package com.sunghyun.plab.subscription.domain.service;

import com.sunghyun.plab.subscription.domain.enums.NotiSetting;
import com.sunghyun.plab.subscription.domain.enums.NotiType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SubscriptionNotificationValidatorTest {

    @Test
    @DisplayName("알림 조건 판단로직 정상 작동하는지 확인")
    void test(){
        //given
        final NotiType settledNotiType = NotiType.PLAYER_COUNT;
        final NotiSetting settledNotiValue = NotiSetting.PLAYER_EIGHT;

        final NotiSetting currentNotiValueOfFreeSub = NotiSetting.SUPER_SUB;
        final NotiSetting currentNotiValueOfPlayerCnt = NotiSetting.PLAYER_NINE;



        //when
        SubscriptionNotificationValidator subscriptionNotificationValidator = new SubscriptionNotificationValidator();
        final boolean flg = subscriptionNotificationValidator.isSatisfied(settledNotiType,settledNotiValue,currentNotiValueOfPlayerCnt,currentNotiValueOfFreeSub);

        //then
        assertThat(flg).isFalse();
    }

}