package com.sunghyun.plab.subscription.application.port.out.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sunghyun.plab.match.domain.enums.ActiveSubType;
import com.sunghyun.plab.match.domain.model.PlabMatch;
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
public class PlabMatchResDto {
    private Long plabMatchNo;
    private String stadiumName;
    private Integer stadiumNo;
    private String matchDt;
    private String matchTm;
    private Integer currentPlayerCnt;
    private ActiveSubType subType;

    public static PlabMatchResDto from(final PlabMatch plabMatch) {
        if (plabMatch == null) return null;

        return PlabMatchResDto.builder()
                .plabMatchNo(plabMatch.getPlabMatchNo())
                .stadiumName(plabMatch.getStadiumName())
                .stadiumNo(plabMatch.getStadiumNo())
                .matchDt(plabMatch.getMatchDt())
                .matchTm(plabMatch.getMatchTm())
                .currentPlayerCnt(plabMatch.getCurrentPlayerCnt())
                .subType(plabMatch.getSubType())
                .build();
    }
}
