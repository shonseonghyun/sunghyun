package com.sunghyun.plab.subscription.application.port.out.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter // 👈 Jackson이 JSON으로 변환할 때 필요합니다.
@Builder
@NoArgsConstructor  // 👈 혹시 모를 역직렬화나 테스트를 위해 기본 생성자 추가
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MatchSubscriptionSummaryDto {

    private Long activeCount;
    private Long inActiveCount;
    private List<String> matchDates;

}