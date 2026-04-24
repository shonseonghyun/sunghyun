package com.sunghyun.plab.subscription.application.port.out.dto;

import com.sunghyun.plab.subscription.domain.enums.NotiSetting;
import com.sunghyun.plab.subscription.domain.enums.NotiType;
import com.sunghyun.plab.subscription.domain.model.MatchSubscription;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MatchSubscriptionModResDto {
    private Long subscriptionNo;
    private Long plabMatchNo;
    private Long memberNo;
    private String email;
    private NotiType notiType;
    private NotiSetting notiValue;

    /**
     * 도메인 엔티티로부터 수정을 위한 응답 DTO를 생성합니다.
     */
    public static MatchSubscriptionModResDto from(MatchSubscription matchSubscription) {
        return MatchSubscriptionModResDto.builder()
                .subscriptionNo(matchSubscription.getSubscriptionNo())
                .plabMatchNo(matchSubscription.getPlabMatchNo())
                .memberNo(matchSubscription.getMemberNo())
                .email(matchSubscription.getEmail())
                .notiType(matchSubscription.getNotiType())
                .notiValue(matchSubscription.getNotiValue())
                .build();
    }
}
