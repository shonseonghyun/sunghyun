package com.sunghyun.schedule.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PlayerLimit {
    private Integer min;
    private Integer max;
}
