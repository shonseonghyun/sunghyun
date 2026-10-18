package com.sunghyun.team.application.port.out.dto;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MonthlyFeeResDto {
    private Integer amount;
    private Integer paymentDay;
}