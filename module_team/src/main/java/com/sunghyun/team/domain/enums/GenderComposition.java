package com.sunghyun.team.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GenderComposition {
    MALE_ONLY("남성"),
    FEMALE_ONLY("여성"),
    MIXED("남녀 혼성")
    ;

    private final String desc;
}
