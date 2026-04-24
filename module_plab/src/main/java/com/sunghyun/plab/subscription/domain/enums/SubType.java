//package com.sunghyun.plab.subscription.domain.enums;
//
//import com.sunghyun.plab.subscription.domain.exception.SubscriptionException;
//import com.sunghyun.web.ErrorCode;
//import lombok.AllArgsConstructor;
//
//import java.util.Arrays;
//
//@AllArgsConstructor
//public enum SubType implements NotiValue{
//    SUPER_SUB(0,"슈퍼 서브"),
//    MANAGER_FREE(1,"매니저 프리")
//    ;
//
//    private final Integer code;
//    private final String desc;
//
//    public static NotiValue fromCode(int value) {
//        return Arrays.stream(SubType.values())
//                .filter(v->v.getCode().equals(value))
//                .findFirst()
//                .orElseThrow(()->new SubscriptionException(ErrorCode.F00))
//                ;
//    }
//
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
