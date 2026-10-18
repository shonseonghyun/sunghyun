package com.sunghyun.schedule.application.dto.req;

import com.sunghyun.schedule.domain.Location;
import lombok.Getter;

@Getter
public class SchedulePollLocationSetReqDto {
    private Long locationNo;

    private String locationName;

    private String address;

    private String roadAddress;

    private String addressX;

    private String addressY;

    public Location toDomain() {
        return Location.builder()
                .locationNo(locationNo)
                .locationName(locationName)
                .address(address)
                .roadAddress(roadAddress)
                .addressX(addressX)
                .addressY(addressY)
                .build()
                ;
    }
}
