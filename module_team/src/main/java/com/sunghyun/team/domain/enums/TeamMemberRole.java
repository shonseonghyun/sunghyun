package com.sunghyun.team.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TeamMemberRole {
    LEADER("회장"),
    SECRETARY("총무"),
    GENERAL("일반 회원")
    ;
    private final String desc;
}
