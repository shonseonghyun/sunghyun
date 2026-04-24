package com.sunghyun.plab.subscription.application.port.in.dto;

import com.sunghyun.annotation.NotNullWithMsg;
import com.sunghyun.plab.subscription.domain.enums.NotiSetting;
import com.sunghyun.plab.subscription.domain.enums.NotiType;
import jakarta.validation.constraints.AssertTrue;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MatchSubscriptionRegReqDto {
    @NotNullWithMsg
    private Long memberNo;          // 사용자 번호

    private String email;           // 알림 받을 이메일

    @NotNullWithMsg
    private Long plabMatchNo;       // 플랩 매치 번호

    private NotiType notiType;

    private NotiSetting value;

    @AssertTrue(message = "선택한 알림 타입과 설정값이 일치하지 않습니다.")
    public boolean isValidSetting(){
        if(notiType == null || value == null){
            return false;
        }
        return value.getNotiType().equals(notiType);
    }

    /**
     * 정적 팩토리 메서드 (선택 사항: DTO 생성을 좀 더 명확하게 하고 싶을 때)
     */
//    public static MatchSubscriptionRegReqDto of(Long memberNo, String email, Long plabMatchNo, Integer targetPlayerCnt, ActiveSubType subType) {
//        return MatchSubscriptionRegReqDto.builder()
//                .memberNo(memberNo)
//                .email(email)
//                .plabMatchNo(plabMatchNo)
//                .targetPlayerCnt(targetPlayerCnt)
//                .subType(subType)
//                .build();
//    }
}
