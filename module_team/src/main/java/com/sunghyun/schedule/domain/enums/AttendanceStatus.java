package com.sunghyun.schedule.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AttendanceStatus {
    ATTENDING("참석"),
    ABSENT("불참"),
    UNDECIDED("미정");

    private final String description;
}