package com.sunghyun.plab.subscription.application.port.in.dto;

import com.sunghyun.annotation.NotNullWithMsg;
import com.sunghyun.plab.subscription.domain.enums.ActiveSubType;
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

    private Integer targetPlayerCnt; // 목표 인원 수

    private ActiveSubType subType;   // 서브 타입 (NONE, SUPER, MANAGER, ALL)

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
