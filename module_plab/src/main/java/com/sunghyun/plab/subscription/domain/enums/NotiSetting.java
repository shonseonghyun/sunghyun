package com.sunghyun.plab.subscription.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum NotiSetting {
    // 인원 수 관련 (PLAYER_COUNT)
    PLAYER_ZERO(NotiType.PLAYER_COUNT, "0", "0명"),
    PLAYER_ONE(NotiType.PLAYER_COUNT, "1", "1명"),
    PLAYER_TWO(NotiType.PLAYER_COUNT, "2", "2명"),
    PLAYER_THREE(NotiType.PLAYER_COUNT, "3", "3명"),
    PLAYER_FOUR(NotiType.PLAYER_COUNT, "4", "4명"),
    PLAYER_FIVE(NotiType.PLAYER_COUNT, "5", "5명"),
    PLAYER_SIX(NotiType.PLAYER_COUNT, "6", "6명"),
    PLAYER_SEVEN(NotiType.PLAYER_COUNT, "7", "7명"),
    PLAYER_EIGHT(NotiType.PLAYER_COUNT, "8", "8명"),
    PLAYER_NINE(NotiType.PLAYER_COUNT, "9", "9명"),
    PLAYER_TEN(NotiType.PLAYER_COUNT, "10", "10명"),
    PLAYER_ELEVEN(NotiType.PLAYER_COUNT, "11", "11명"),
    PLAYER_TWELVE(NotiType.PLAYER_COUNT, "12", "12명"),
    PLAYER_THIRTEEN(NotiType.PLAYER_COUNT, "13", "13명"),
    PLAYER_FOURTEEN(NotiType.PLAYER_COUNT, "14", "14명"),
    PLAYER_FIFTEEN(NotiType.PLAYER_COUNT, "15", "15명"),
    PLAYER_SIXTEEN(NotiType.PLAYER_COUNT, "16", "16명"),
    PLAYER_SEVENTEEN(NotiType.PLAYER_COUNT, "17", "17명"),
    PLAYER_EIGHTEEN(NotiType.PLAYER_COUNT, "18", "18명"),

    // 서브 타입 관련 (FREE_SUB)
    NONE(NotiType.FREE_SUB, "NONE", "활성화 x"),
    SUPER_SUB(NotiType.FREE_SUB, "SUPER_SUB", "슈퍼 서브"),
    MANAGER_FREE(NotiType.FREE_SUB, "MANAGER_FREE", "매니저 프리")
    ;

    private final NotiType notiType;  // 이 설정이 속한 그룹 (PLAYER_COUNT or FREE_SUB)
    private final String code;   // DB에 저장하거나 비즈니스 로직에 쓸 코드값
    private final String desc;

    public static NotiSetting fromCode(String code){
        if(code == null) throw new IllegalArgumentException("s");

        return Arrays.stream(NotiSetting.values())
                .filter(notiSetting -> notiSetting.getCode().equals(code))
                .findFirst()
                .orElseThrow(()->new IllegalArgumentException("blabal"));
    }
}
