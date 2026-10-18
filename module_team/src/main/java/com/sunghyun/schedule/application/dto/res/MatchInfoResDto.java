package com.sunghyun.schedule.application.dto.res;

import com.sunghyun.schedule.domain.MatchInfo;
import com.sunghyun.schedule.domain.enums.MatchType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MatchInfoResDto {
    private MatchType matchType;
    private LocationResDto location;
    private String playDt;
    private String playTm;

    public static MatchInfoResDto from(MatchInfo matchInfo) {
        if (matchInfo == null) {
            return null;
        }
        return MatchInfoResDto.builder()
                .matchType(matchInfo.getMatchType())
                .location(LocationResDto.from(matchInfo.getLocation()))
                .playDt(matchInfo.getPlayDt())
                .playTm(matchInfo.getPlayTm())
                .build();
    }
}