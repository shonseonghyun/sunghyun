package com.sunghyun.team.domain.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MonthlyFee {
    private Long monthlyFeeNo;

    private Integer amount;
    private Integer paymentDay;

    public MonthlyFee(Integer amount,Integer paymentDay){
        this.amount = amount;
        this.paymentDay = paymentDay;
    }
}
