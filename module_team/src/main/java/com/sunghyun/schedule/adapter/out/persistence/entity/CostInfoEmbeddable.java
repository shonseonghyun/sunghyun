package com.sunghyun.schedule.adapter.out.persistence.entity;

import com.sunghyun.schedule.domain.CostInfo;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CostInfoEmbeddable {

    @Column
    private Integer expectedTotalCost;

    public static CostInfoEmbeddable from(CostInfo domain) {
        if (domain == null) return null;
        return new CostInfoEmbeddable(domain.getExpectedTotalCost());
    }

    public CostInfo toDomain() {
        return new CostInfo(this.expectedTotalCost);
    }
}