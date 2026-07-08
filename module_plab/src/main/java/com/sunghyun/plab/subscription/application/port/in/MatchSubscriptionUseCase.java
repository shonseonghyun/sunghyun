package com.sunghyun.plab.subscription.application.port.in;

import com.sunghyun.plab.subscription.application.port.in.dto.MatchSubscriptionModReqDto;
import com.sunghyun.plab.subscription.application.port.in.dto.MatchSubscriptionRegReqDto;
import com.sunghyun.plab.subscription.application.port.out.dto.*;

import java.util.List;

public interface MatchSubscriptionUseCase {
    MatchSubscriptionRegResDto registerMatchSubscription(final MatchSubscriptionRegReqDto dto);
    MatchSubscriptionModResDto modifyMatchSubscription(final Long subscriptionNo,final MatchSubscriptionModReqDto dto);
    MatchSubscriptionSummaryDto getMatchSubscriptionsSummary(final Long memberNo, final String startDate, final String endDt);
    List<MatchSubscriptionSelResDto> getMatchSubscriptionsByDate(final Long memberNo, final String targetDate);
    void toggleSubscriptionStatus(final Long subscriptionNo,final Long memberNo);
}
