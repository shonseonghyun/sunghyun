package com.sunghyun.feign.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@Builder
@Getter
@NoArgsConstructor
public class InternalGuestRecruitmentDto {
    private Long recruitmentNo;
    private List<InternalGuestApplyerDto> guestApplyers;

    public static InternalGuestRecruitmentDto of(Long recruitmentNo, List<InternalGuestApplyerDto> internalGuestApplyerDtos) {
        return InternalGuestRecruitmentDto.builder()
                .recruitmentNo(recruitmentNo)
                .guestApplyers(internalGuestApplyerDtos)
                .build()
                ;
    }
}
