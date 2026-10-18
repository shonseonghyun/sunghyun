package com.sunghyun.guest.application.dto;

import com.sunghyun.guest.domain.Position;
import com.sunghyun.guest.domain.PositionRequirement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class PositionRequirementResDto {
    private Position position;
    private int requiredCount;
    private int confirmedCount;
//    private int remainingCount;

    public static PositionRequirementResDto of(PositionRequirement domain,int confirmedCount){
//        int remainingCount = Math.max(0, domain.getRequiredCount() - confirmedCount);

        return PositionRequirementResDto.builder()
                .position(domain.getPosition())
                .requiredCount(domain.getRequiredCount())
                .confirmedCount(confirmedCount)
                .build()
                ;
    }
}
