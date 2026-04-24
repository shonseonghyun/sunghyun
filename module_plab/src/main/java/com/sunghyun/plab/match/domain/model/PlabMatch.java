package com.sunghyun.plab.match.domain.model;

import com.sunghyun.feign.dto.PlabMatchResponseDto;
import com.sunghyun.plab.match.domain.enums.ActiveSubType;
import com.sunghyun.plab.match.domain.exception.InvalidPlabMatchException;
import com.sunghyun.utils.ApiUtils;
import com.sunghyun.web.ErrorCode;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Builder
@Entity
@Table(name = "plab_match")
@NoArgsConstructor
@AllArgsConstructor
public class PlabMatch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long matchNo;

    @Column
    private Long plabMatchNo;

    @Column
    private String stadiumName;

//    @Column
//    @Embeddable
//    private StadiumInfo stadiumInfo;
    @Column
    private Integer stadiumNo;

    @Column
    private String matchDt;

    @Column
    private String matchTm;

    @Column
    private Integer currentPlayerCnt;

    @Column
    @Enumerated(EnumType.STRING)
    private ActiveSubType subType;

    public static PlabMatch create(
            final Long plabMatchNo,
            final PlabMatchResponseDto result
    )
    {
        if(result == null){
            //200 응답받았으나 실제 데이터가 존재하지 않는 경우
            throw new InvalidPlabMatchException(ErrorCode.P02);
        }

        //등록 요청한 매치가 이미 지난 매치인 경우
        if (ApiUtils.isPastSchedule(result.getSchedule())) {
            throw new InvalidPlabMatchException(ErrorCode.P03); // 지난 매치 에러 코드
        }

        PlabMatch plabMatch = PlabMatch.builder()
                .plabMatchNo(plabMatchNo)
                .stadiumName(result.getLabelStadium())
                .stadiumNo(result.getStadiumGroupId())
                .matchDt(ApiUtils.parseDate(result.getSchedule()))
                .matchTm(ApiUtils.parseTime(result.getSchedule()))
                .currentPlayerCnt(result.getTotalApplyCnt())
                .subType(ActiveSubType.getSubType(result.isSuperSub(),result.isManagerFree()))
                .build()
                ;

        return plabMatch;
    }
}
