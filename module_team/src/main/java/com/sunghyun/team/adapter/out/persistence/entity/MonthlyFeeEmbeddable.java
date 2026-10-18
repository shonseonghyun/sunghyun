package com.sunghyun.team.adapter.out.persistence.entity;

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
public class MonthlyFeeEmbeddable {

    @Column(name = "monthly_fee_amount")
    private Integer amount;

    @Column(name = "monthly_fee_payment_day")
    private Integer paymentDay;
}