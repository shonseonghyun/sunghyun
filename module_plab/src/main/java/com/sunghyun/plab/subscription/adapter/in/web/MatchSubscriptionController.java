package com.sunghyun.plab.subscription.adapter.in.web;

import com.sunghyun.plab.subscription.application.port.in.MatchSubscriptionUseCase;
import com.sunghyun.plab.subscription.application.port.in.dto.MatchSubscriptionModReqDto;
import com.sunghyun.plab.subscription.application.port.in.dto.MatchSubscriptionRegReqDto;
import com.sunghyun.plab.subscription.application.port.out.dto.MatchSubscriptionModResDto;
import com.sunghyun.plab.subscription.application.port.out.dto.MatchSubscriptionRegResDto;
import com.sunghyun.web.ErrorCode;
import com.sunghyun.web.GlobalResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/plab/subscription")
public class MatchSubscriptionController {

    private final MatchSubscriptionUseCase matchSubscriptionUseCase;


    @PostMapping("")
    public GlobalResponse<MatchSubscriptionRegResDto> registerMatchSubscription(
            @Valid @RequestBody final MatchSubscriptionRegReqDto dto
    )
    {
        MatchSubscriptionRegResDto result = matchSubscriptionUseCase.registerMatchSubscription(dto);
        return GlobalResponse.of(ErrorCode.S00,result);
    }

    @PutMapping("/{subscriptionNo}")
    public GlobalResponse<MatchSubscriptionModResDto> modifyMatchSubscription(
            @PathVariable("subscriptionNo") final Long subscriptionNo,
            @Valid @RequestBody final MatchSubscriptionModReqDto dto
    )
    {
        MatchSubscriptionModResDto result = matchSubscriptionUseCase.modifyMatchSubscription(subscriptionNo,dto);
        return GlobalResponse.of(ErrorCode.S00,result);
    }
}
