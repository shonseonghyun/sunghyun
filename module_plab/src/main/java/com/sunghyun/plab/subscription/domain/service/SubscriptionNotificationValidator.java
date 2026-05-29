package com.sunghyun.plab.subscription.domain.service;

import com.sunghyun.plab.subscription.domain.enums.NotiSetting;
import com.sunghyun.plab.subscription.domain.enums.NotiType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SubscriptionNotificationValidator {

    public boolean isSatisfied(final NotiType notiType, final NotiSetting notiValue, final NotiSetting currentPlayerCnt, final NotiSetting currentSubType){
        final NotiSetting currentValue = notiType.equals(NotiType.PLAYER_COUNT)
                ? currentPlayerCnt
                : currentSubType
                ;
        return notiValue.equals(currentValue);
    }
}
