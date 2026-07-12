package com.sunghyun.plab.subscription.adapter.in.web;

import com.sunghyun.annotation.AuthMember;
import com.sunghyun.dto.AuthMemberInfo;
import com.sunghyun.plab.subscription.application.port.in.MatchSubscriptionUseCase;
import com.sunghyun.plab.subscription.application.port.in.dto.MatchSubscriptionModReqDto;
import com.sunghyun.plab.subscription.application.port.in.dto.MatchSubscriptionRegReqDto;
import com.sunghyun.plab.subscription.application.port.out.dto.*;
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

    @GetMapping("/subscriptions")
    public GlobalResponse getMatchSubscriptions(@RequestParam(required = false) String date,@AuthMember AuthMemberInfo authMember) {
        final Long memberNo = authMember.getMemberNo();
        final String targetDate = (date == null) ? ApiUtils.getCurrentDt() : date;
        List<MatchSubscriptionSelResDto> result = matchSubscriptionUseCase.getMatchSubscriptionsByDate(memberNo,targetDate);
        return GlobalResponse.of(ErrorCode.S000,result);
    }

    @GetMapping("/subscriptions/summary")
    public GlobalResponse getMatchSubscriptionsSummary(@RequestParam(required = false) String month,@AuthMember AuthMemberInfo authMember) {
        final Long memberNo = authMember.getMemberNo();
        final String startDate = ApiUtils.getStartOfMonth(month);
        final String endDate = ApiUtils.getEndOfMonth(month);
        MatchSubscriptionSummaryDto result = matchSubscriptionUseCase.getMatchSubscriptionsSummary(memberNo,startDate,endDate);
        return GlobalResponse.of(ErrorCode.S000,result);
    }

    @PostMapping("/subscription")
    public GlobalResponse<MatchSubscriptionRegResDto> registerMatchSubscription(@Valid @RequestBody final MatchSubscriptionRegReqDto dto,@AuthMember AuthMemberInfo authMemberInfo) {
        final Long memberNo = authMemberInfo.getMemberNo();

        MatchSubscriptionRegResDto result = matchSubscriptionUseCase.registerMatchSubscription(memberNo,dto);
        return GlobalResponse.of(ErrorCode.S000,result);
    }

    @PutMapping("/subscription/{subscriptionNo}")
    public GlobalResponse<MatchSubscriptionModResDto> modifyMatchSubscription(@PathVariable final Long subscriptionNo,@Valid @RequestBody final MatchSubscriptionModReqDto dto) {
        MatchSubscriptionModResDto result = matchSubscriptionUseCase.modifyMatchSubscription(subscriptionNo,dto);
        return GlobalResponse.of(ErrorCode.S000,result);
    }

    @PutMapping("/subscriptions/{subscriptionNo}/status")
    public GlobalResponse toggleSubscriptionStatus(@PathVariable final Long subscriptionNo,@AuthMember AuthMemberInfo authMemberInfo) {
        final Long memberNo = authMemberInfo.getMemberNo();
        matchSubscriptionUseCase.toggleSubscriptionStatus(subscriptionNo,memberNo);
        return GlobalResponse.of(ErrorCode.S000);
    }
}
