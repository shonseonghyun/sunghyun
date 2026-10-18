package com.sunghyun.schedule.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CostInfo {
    private Integer expectedTotalCost;

    public Integer calculateCostPerPerson(int size) {
// 총 금액이 없거나 인원수가 1명 미만(0명 이하)인 경우 0원 반환 (0으로 나누기 방지)
        if (expectedTotalCost == null || expectedTotalCost <= 0 || size <= 0) {
            return 0;
        }

        return expectedTotalCost / size;
    }
}
