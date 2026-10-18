package com.sunghyun.guest.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class GuestApplyer {
    private Long applicationNo;

    private Long memberNo;

    private Position position;

    private ApplicationStatus applicationStatus;

    private String createdDt;
    private String createdTm;

    private String modifiedDt;
    private String modifiedTm;

    public static GuestApplyer createConfirmed(Long applyedMemberNo, Position applyedPosition) {
        return GuestApplyer.builder()
                .memberNo(applyedMemberNo)
                .position(applyedPosition)
                .applicationStatus(ApplicationStatus.CONFIRMED)
                .build()
                ;
    }
}
