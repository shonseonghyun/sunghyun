package com.sunghyun.plab.subscription.application.port.in;

import com.sunghyun.plab.subscription.application.port.in.dto.MatchSubscriptionModReqDto;
import com.sunghyun.plab.subscription.application.port.in.dto.MatchSubscriptionRegReqDto;
import com.sunghyun.plab.subscription.application.port.out.dto.MatchSubscriptionModResDto;
import com.sunghyun.plab.subscription.application.port.out.dto.MatchSubscriptionRegResDto;
import com.sunghyun.plab.subscription.application.port.out.dto.MatchSubscriptionsSelResDto;

import java.util.List;

public interface MatchSubscriptionUseCase {
    MatchSubscriptionRegResDto registerMatchSubscription(final MatchSubscriptionRegReqDto dto);
    MatchSubscriptionModResDto modifyMatchSubscription(final Long subscriptionNo,final MatchSubscriptionModReqDto dto);
    List<MatchSubscriptionsSelResDto> getMatchSubscriptions(final Long memberNo, final String startDate, final String endDt);
}
