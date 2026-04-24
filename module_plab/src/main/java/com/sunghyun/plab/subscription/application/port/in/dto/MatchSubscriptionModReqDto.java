package com.sunghyun.plab.subscription.application.port.in.dto;

import com.sunghyun.plab.subscription.domain.enums.NotiSetting;
import com.sunghyun.plab.subscription.domain.enums.NotiType;
import com.sunghyun.plab.subscription.domain.model.MatchSubscription;
import jakarta.validation.constraints.AssertTrue;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MatchSubscriptionModReqDto {
    private NotiType notiType;
    private NotiSetting value;

    public MatchSubscription toDomain(final NotiType notiType) {
        //upateable 등록된 필드들로만 도메인 만들도록 자동화 못하나?
        //왜냐면 매번 수정dto생길때마다 이렇게 길게 써야할텐데..
        return MatchSubscription.builder()
                .notiValue(value)
                .build()
                ;
    }

    @AssertTrue(message = "선택한 알림 타입과 설정값이 일치하지 않습니다.")
    public boolean isValidSetting(){
        if(notiType == null || value == null){
            return false;
        }
        return value.getNotiType().equals(notiType);
    }
}
