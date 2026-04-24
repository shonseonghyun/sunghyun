package com.sunghyun.plab.subscription.application.port.out.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sunghyun.plab.subscription.domain.enums.NotiSetting;
import com.sunghyun.plab.subscription.domain.enums.NotiType;
import com.sunghyun.plab.subscription.domain.model.MatchSubscription;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MatchSubscriptionRegResDto {
    private Long subscriptionNo;
    private Long plabMatchNo;
    private Long memberNo;
    private String email;
    private NotiType notiType;
    private NotiSetting notiValue;
    private PlabMatchResDto plabMatchResDto;

    public static MatchSubscriptionRegResDto from(final MatchSubscription matchSubscription) {
        return MatchSubscriptionRegResDto.builder()
                .subscriptionNo(matchSubscription.getSubscriptionNo())
                .plabMatchNo(matchSubscription.getPlabMatchNo())
                .memberNo(matchSubscription.getMemberNo())
                .email(matchSubscription.getEmail())
                .notiType(matchSubscription.getNotiType())
                .notiValue(matchSubscription.getNotiValue())
                .build();
    }
    public static MatchSubscriptionRegResDto from(final MatchSubscription matchSubscription, final PlabMatchResDto plabMatchResDto) {
        return MatchSubscriptionRegResDto.builder()
                .subscriptionNo(matchSubscription.getSubscriptionNo())
                .plabMatchNo(matchSubscription.getPlabMatchNo())
                .memberNo(matchSubscription.getMemberNo())
                .email(matchSubscription.getEmail())
                .notiType(matchSubscription.getNotiType())
                .notiValue(matchSubscription.getNotiValue())
                .plabMatchResDto(plabMatchResDto)
                .build();
    }
}
