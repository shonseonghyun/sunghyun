package com.sunghyun.plab.subscription.application.port.out.dto;

import com.sunghyun.plab.subscription.domain.enums.ActiveSubType;
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
    private Integer targetPlayerCnt;
    private ActiveSubType subType;
    private boolean isNotified;

    /**
     * 도메인 엔티티로부터 수정을 위한 응답 DTO를 생성합니다.
     */
    public static MatchSubscriptionModResDto from(MatchSubscription entity) {
        return MatchSubscriptionModResDto.builder()
                .subscriptionNo(entity.getSubscriptionNo())
                .plabMatchNo(entity.getPlabMatchNo())
                .memberNo(entity.getMemberNo())
                .email(entity.getEmail())
                .targetPlayerCnt(entity.getTargetPlayerCnt())
                .subType(entity.getSubType())
                .isNotified(entity.isNotified())
                .build();
    }
}
