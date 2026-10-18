package com.sunghyun.schedule.domain;

import com.sunghyun.schedule.domain.enums.MatchType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MatchInfo {
    private MatchType matchType;
    private Location location;
    private String playDt;
    private String playTm;

    public void setLocation(Location location){
        this.location = location;
    }
}
