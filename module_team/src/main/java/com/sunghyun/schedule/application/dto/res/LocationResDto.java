package com.sunghyun.schedule.application.dto.res;

import com.sunghyun.feign.dto.InternalLocationResponseDto;
import com.sunghyun.schedule.domain.Location;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class LocationResDto {
    private String locationName;

    private String address;

    private String roadAddress;

    private String addressX;

    private String addressY;

    public static LocationResDto from(Location location) {
        if (location == null) {
            return null;
        }
        return LocationResDto.builder()
                .locationName(location.getLocationName())
                .address(location.getAddress())
                .roadAddress(location.getRoadAddress())
                .addressX(location.getAddressX())
                .addressY(location.getAddressY())
                .build()
                ;
    }

    public static LocationResDto from(InternalLocationResponseDto location) {
        if (location == null) {
            return null;
        }
        return LocationResDto.builder()
                .locationName(location.getLocationName())
                .address(location.getAddress())
                .roadAddress(location.getRoadAddress())
                .addressX(location.getAddressX())
                .addressY(location.getAddressY())
                .build()
                ;
    }
}
