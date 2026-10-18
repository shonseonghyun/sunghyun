package com.sunghyun.guest.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LevelLimit {
    ALL("누구나"),
    BEGINNER("비기너"),
    AMATEUR("아마추어"),
    SEMI_PRO("세미 프로"),
    PRO("프로")
    ;
    private final String desc;

}
