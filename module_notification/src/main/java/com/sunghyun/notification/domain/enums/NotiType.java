package com.sunghyun.notification.domain.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NotiType {
    PLAYER_COUNT("충족 인원 알림"
//            , Arrays.asList(
//            NotiSetting.PLAYER_ZERO, NotiSetting.PLAYER_ONE, NotiSetting.PLAYER_TWO,
//            NotiSetting.PLAYER_THREE, NotiSetting.PLAYER_FOUR, NotiSetting.PLAYER_FIVE,
//            NotiSetting.PLAYER_SIX, NotiSetting.PLAYER_SEVEN, NotiSetting.PLAYER_EIGHT,
//            NotiSetting.PLAYER_NINE, NotiSetting.PLAYER_TEN, NotiSetting.PLAYER_ELEVEN,
//            NotiSetting.PLAYER_TWELVE, NotiSetting.PLAYER_THIRTEEN, NotiSetting.PLAYER_FOURTEEN,
//            NotiSetting.PLAYER_FIFTEEN, NotiSetting.PLAYER_SIXTEEN, NotiSetting.PLAYER_SEVENTEEN,
//            NotiSetting.PLAYER_EIGHTEEN)
            ),

    FREE_SUB("프리 서브 알림"
//                     , Arrays.asList(
//            NotiSetting.FREE_SUB_SUPER_SUB,
//            NotiSetting.FREE_SUB_MANAGER_FREE)
            )
    ;

    private final String desc;
//    private final List<NotiSetting> settings; // 이 타입이 가질 수 있는 상세 설정들
}
