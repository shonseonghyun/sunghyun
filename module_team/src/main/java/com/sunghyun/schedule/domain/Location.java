package com.sunghyun.schedule.domain;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class Location {
    @NotNull
    private Long locationNo;

    private String locationName;

    private String address;

    private String roadAddress;

    private String addressX;

    private String addressY;

    public Location(Long locationNo){
        this.locationNo = locationNo;
    }
}
