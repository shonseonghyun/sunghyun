//package com.sunghyun.plab.subscription.domain.enums;
//
//import com.sunghyun.plab.subscription.domain.exception.SubscriptionException;
//import com.sunghyun.web.ErrorCode;
//import lombok.AllArgsConstructor;
//
//import java.util.Arrays;
//
//@AllArgsConstructor
//public enum PlayerCount implements NotiValue{
//    ZERO(0, "0명"), ONE(1, "1명"), TWO(2, "2명"), THREE(3, "3명"),
//    FOUR(4, "4명"), FIVE(5, "5명"), SIX(6, "6명"), SEVEN(7, "7명"),
//    EIGHT(8, "8명"), NINE(9, "9명"), TEN(10, "10명"), ELEVEN(11, "11명"),
//    TWELVE(12, "12명"), THIRTEEN(13, "13명"), FOURTEEN(14, "14명"),
//    FIFTEEN(15, "15명"), SIXTEEN(16, "16명"), SEVENTEEN(17, "17명"),
//    EIGHTEEN(18, "18명");
//
//    private final Integer code;
//    private final String desc;
//
//    public static NotiValue fromCode(int value) {
//        return Arrays.stream(PlayerCount.values())
//                .filter(v->v.getCode().equals(value))
//                .findFirst()
//                .orElseThrow(()->new SubscriptionException(ErrorCode.F00))
//                ;
//    }
//
//    @Override
//    public Integer getCode() {
//        return this.code;
//    }
//
//    @Override
//    public String getDesc() {
//        return this.desc;
//    }
//}
