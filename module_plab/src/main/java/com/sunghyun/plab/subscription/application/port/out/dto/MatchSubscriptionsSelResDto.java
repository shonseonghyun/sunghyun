package com.sunghyun.plab.subscription.application.port.out.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class MatchSubscriptionsSelResDto {
    private PlabMatchResDto plabMatch;
    private MatchSubscriptionSelResDto matchSubscription;
}
