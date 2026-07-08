package com.sunghyun.plab.subscription.application.port.out.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sunghyun.plab.match.domain.enums.MatchStatus;
import com.sunghyun.plab.match.domain.model.PlabMatch;
import com.sunghyun.plab.subscription.domain.enums.NotiSetting;
import com.sunghyun.plab.subscription.domain.model.PlabMatchData;
import lombok.*;

/*
*  in.web.dto에 들어가는 게 맞으나 계층 의존성이 맞지 않아 해당 레이어에 둔다.
* */

@ToString
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlabMatchResDto implements PlabMatchData {
    private Long plabMatchNo;
    private String stadiumName;
    private Integer stadiumNo;
    private String matchDt;
    private String matchTm;
    private NotiSetting playerCnt;
    private Integer maxPlayerCnt;
    private NotiSetting subType;
    private MatchStatus status;

    public static PlabMatchResDto from(final PlabMatch plabMatch) {
        if (plabMatch == null) return null;

        return PlabMatchResDto.builder()
                .plabMatchNo(plabMatch.getPlabMatchNo())
                .stadiumName(plabMatch.getStadiumName())
                .stadiumNo(plabMatch.getStadiumNo())
                .matchDt(plabMatch.getMatchDt())
                .matchTm(plabMatch.getMatchTm())
                .playerCnt(plabMatch.getPlayerCnt())
                .maxPlayerCnt(plabMatch.getMaxPlayerCnt())
                .subType(plabMatch.getSubType())
                .status(plabMatch.getStatus())
                .build();
    }
}
