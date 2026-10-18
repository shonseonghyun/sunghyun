package com.sunghyun.schedule.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MatchType {
    TEAM("매칭"),
    INTERNAL("자체전")
    ;

    private final String desc;
}
