package com.sunghyun.feign.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class InternalLocationResponseDto {
    private String locationName;

    private String address;

    private String roadAddress;

    private String addressX;

    private String addressY;
}
