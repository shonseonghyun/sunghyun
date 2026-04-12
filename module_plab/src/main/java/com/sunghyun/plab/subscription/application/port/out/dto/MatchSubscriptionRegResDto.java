package com.sunghyun.plab.subscription.application.port.out.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sunghyun.plab.subscription.domain.enums.ActiveSubType;
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
    private Integer targetPlayerCnt;
    private ActiveSubType subType;
    private boolean isNotified;

    private PlabMatchResDto plabMatchResDto;

    public static MatchSubscriptionRegResDto from(final MatchSubscription matchSubscription) {
        return MatchSubscriptionRegResDto.builder()
                .subscriptionNo(matchSubscription.getSubscriptionNo())
                .plabMatchNo(matchSubscription.getPlabMatchNo())
                .memberNo(matchSubscription.getMemberNo())
                .email(matchSubscription.getEmail())
                .targetPlayerCnt(matchSubscription.getTargetPlayerCnt())
                .subType(matchSubscription.getSubType())
                .isNotified(matchSubscription.isNotified())
                .build();
    }
    public static MatchSubscriptionRegResDto from(final MatchSubscription matchSubscription, final PlabMatchResDto plabMatchResDto) {
        return MatchSubscriptionRegResDto.builder()
                .subscriptionNo(matchSubscription.getSubscriptionNo())
                .plabMatchNo(matchSubscription.getPlabMatchNo())
                .memberNo(matchSubscription.getMemberNo())
                .email(matchSubscription.getEmail())
                .targetPlayerCnt(matchSubscription.getTargetPlayerCnt())
                .subType(matchSubscription.getSubType())
                .isNotified(matchSubscription.isNotified())
                .plabMatchResDto(plabMatchResDto)
                .build();
    }
}
