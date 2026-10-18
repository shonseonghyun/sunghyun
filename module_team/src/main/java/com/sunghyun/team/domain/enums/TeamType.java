package com.sunghyun.team.domain.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum TeamType {
    FUTSAL("풋살"),
    SOCCER("축구")
    ;

    private final String desc;
}
