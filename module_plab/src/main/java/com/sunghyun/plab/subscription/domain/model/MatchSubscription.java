package com.sunghyun.plab.subscription.domain.model;

import com.sunghyun.annotation.UpdateAble;
import com.sunghyun.plab.subscription.domain.enums.NotiSetting;
import com.sunghyun.plab.subscription.domain.enums.NotiType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MatchSubscription {
    private Long subscriptionNo;

    private Long plabMatchNo;

    private Long memberNo;

    private String email;

    private NotiType notiType;

    @UpdateAble
    private NotiSetting notiValue;

    private boolean isActive;

    public static MatchSubscription create(
            final Long plabMatchNo,
            final Long memberNo,
            final String email,
            final NotiType notiType,
            final NotiSetting notiValue,
            final boolean isActive
    )
    {
        return MatchSubscription.builder()
                .plabMatchNo(plabMatchNo)
                .memberNo(memberNo)
                .email(email)
                .notiType(notiType)
                .notiValue(notiValue)
                .isActive(isActive)
                .build()
                ;
    }

    public void toggleStatus() {
        isActive = !isActive;
    }
}
