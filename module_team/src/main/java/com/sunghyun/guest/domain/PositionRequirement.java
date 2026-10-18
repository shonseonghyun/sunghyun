package com.sunghyun.guest.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class PositionRequirement {
    private Long requirementNo;
    private Position position;
    private int requiredCount;
}
