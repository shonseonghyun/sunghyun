package com.sunghyun.plab.subscription.application.port.in;

import com.sunghyun.plab.subscription.application.port.in.dto.MatchSubscriptionModReqDto;
import com.sunghyun.plab.subscription.application.port.in.dto.MatchSubscriptionRegReqDto;
import com.sunghyun.plab.subscription.application.port.out.dto.MatchSubscriptionModResDto;
import com.sunghyun.plab.subscription.application.port.out.dto.MatchSubscriptionRegResDto;

public interface MatchSubscriptionUseCase {
    MatchSubscriptionRegResDto registerMatchSubscription(final MatchSubscriptionRegReqDto dto);
    MatchSubscriptionModResDto modifyMatchSubscription(final Long subscriptionNo,final MatchSubscriptionModReqDto dto);
}
