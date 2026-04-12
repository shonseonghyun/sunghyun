package com.sunghyun.plab.subscription.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ActiveSubType {
    NONE("N","활성화된 프리서브가 없음"),
    SUPER_SUB("S","슈퍼서브 활성화"),
    MANAGER_SUB("M","매니저 서브 활성화"),
    ALL("A","모두 활성화")
    ;

    private final String code;
    private final String desc;

    public static ActiveSubType getSubType(boolean superSub, boolean managerFree) {
        if (superSub && managerFree) return ALL;
        if (superSub) return SUPER_SUB;
        if (managerFree) return MANAGER_SUB;
        return NONE;

//        if(!superSub && !managerFree) return NONE;
//        else if(superSub && !managerFree) return SUPER_SUB;
//        else if(!superSub && managerFree) return MANAGER_SUB;
//        else return ALL;
    }
}
