package com.sunghyun.plab.subscription.application.port.in.dto;

import com.sunghyun.plab.subscription.domain.enums.ActiveSubType;
import com.sunghyun.plab.subscription.domain.model.MatchSubscription;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MatchSubscriptionModReqDto {
    //아예 값이 안 들어오거나 1이상 들어와야 한다.
    @Min(value = 1,message = "{plab.subscription.targetPlayerCnt.min}")
    private Integer targetPlayerCnt;

    //아예 값이 안들어올 수 있다.
    private ActiveSubType subType;

    public MatchSubscription toDomain() {
        //upateable 등록된 필드들로만 도메인 만들도록 자동화 못하나?
        //왜냐면 매번 수정dto생길때마다 이렇게 길게 써야할텐데..
        return MatchSubscription.builder()
//                .subscriptionNo(this.subscriptionNo)
                .targetPlayerCnt(this.targetPlayerCnt)
                .subType(this.subType)
                .build()
                ;
    }
}
