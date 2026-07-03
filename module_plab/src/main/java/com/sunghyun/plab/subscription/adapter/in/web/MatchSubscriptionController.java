package com.sunghyun.plab.subscription.adapter.in.web;

import com.sunghyun.annotation.AuthMember;
import com.sunghyun.dto.AuthMemberInfo;
import com.sunghyun.plab.subscription.application.port.in.MatchSubscriptionUseCase;
import com.sunghyun.plab.subscription.application.port.in.dto.MatchSubscriptionModReqDto;
import com.sunghyun.plab.subscription.application.port.in.dto.MatchSubscriptionRegReqDto;
import com.sunghyun.plab.subscription.application.port.out.dto.MatchSubscriptionModResDto;
import com.sunghyun.plab.subscription.application.port.out.dto.MatchSubscriptionRegResDto;
import com.sunghyun.plab.subscription.application.port.out.dto.MatchSubscriptionsSelResDto;
import com.sunghyun.utils.ApiUtils;
import com.sunghyun.web.ErrorCode;
import com.sunghyun.web.GlobalResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/plab")
public class MatchSubscriptionController {

    private final MatchSubscriptionUseCase matchSubscriptionUseCase;

    @GetMapping("/subscription")
    public GlobalResponse getMatchSubscription(@RequestParam(required = false) String date,@AuthMember AuthMemberInfo authMember) {
        final Long memberNo = authMember.getMemberNo();
        final String targetDate = (date == null) ? ApiUtils.getCurrentDt() : date;
        List<MatchSubscriptionsSelResDto> result = matchSubscriptionUseCase.getMatchSubscriptionsByDate(memberNo,targetDate);
        return GlobalResponse.of(ErrorCode.S000,result);
    }


    @GetMapping("/subscriptions")
    public GlobalResponse getMatchSubscriptions(@RequestParam(required = false) String month,@AuthMember AuthMemberInfo authMember) {
        final Long memberNo = authMember.getMemberNo();
        final String startDate = ApiUtils.getStartOfMonth(month);
        final String endDate = ApiUtils.getEndOfMonth(month);
        List<MatchSubscriptionsSelResDto> result = matchSubscriptionUseCase.getMatchSubscriptions(memberNo,startDate,endDate);
        return GlobalResponse.of(ErrorCode.S000,result);
    }

    @PostMapping("/subscription")
    public GlobalResponse<MatchSubscriptionRegResDto> registerMatchSubscription(
            @Valid @RequestBody final MatchSubscriptionRegReqDto dto
    )
    {
        MatchSubscriptionRegResDto result = matchSubscriptionUseCase.registerMatchSubscription(dto);
        return GlobalResponse.of(ErrorCode.S000,result);
    }

    @PutMapping("/subscription/{subscriptionNo}")
    public GlobalResponse<MatchSubscriptionModResDto> modifyMatchSubscription(
            @PathVariable final Long subscriptionNo,
            @Valid @RequestBody final MatchSubscriptionModReqDto dto
    )
    {
        MatchSubscriptionModResDto result = matchSubscriptionUseCase.modifyMatchSubscription(subscriptionNo,dto);
        return GlobalResponse.of(ErrorCode.S000,result);
    }
}
