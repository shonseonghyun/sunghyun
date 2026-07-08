package com.sunghyun.plab.match.domain.model;

import com.sunghyun.feign.dto.PlabMatchResponseDto;
import com.sunghyun.plab.match.adapter.out.persistence.MatchStatusConverter;
import com.sunghyun.plab.match.domain.enums.MatchStatus;
import com.sunghyun.plab.match.domain.exception.InvalidPlabMatchException;
import com.sunghyun.plab.subscription.adapter.out.persistence.converter.NotiSettingConverter;
import com.sunghyun.plab.subscription.domain.enums.NotiSetting;
import com.sunghyun.utils.ApiUtils;
import com.sunghyun.web.ErrorCode;
import jakarta.persistence.*;
import lombok.*;


@Setter
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
    @Convert(converter = NotiSettingConverter.class)
    private NotiSetting playerCnt;

    @Column
    private Integer maxPlayerCnt;

    @Column
    @Convert(converter = NotiSettingConverter.class)
    private NotiSetting subType;

    @Column
    @Convert(converter = MatchStatusConverter.class)
    private MatchStatus status;

    public void validateActiveStatus(){
        if(isCanceled()){
            throw new InvalidPlabMatchException(ErrorCode.P05);
        }
    }

    private boolean isCanceled(){
        return this.status == MatchStatus.CANCELED;
    }

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
            throw new InvalidPlabMatchException(ErrorCode.P04); // 지난 매치 에러 코드
        }

        PlabMatch plabMatch = PlabMatch.builder()
                .plabMatchNo(plabMatchNo)
                .stadiumName(result.getLabelStadium())
                .stadiumNo(result.getStadiumGroupId())
                .matchDt(ApiUtils.parseDate(result.getSchedule()))
                .matchTm(ApiUtils.parseTime(result.getSchedule()))
                .playerCnt(NotiSetting.fromCode(String.valueOf(result.getTotalApplyCnt())))
                .maxPlayerCnt(result.getMaxPlayerCnt())
                .subType(NotiSetting.getSubType(result.isSuperSub(),result.isManagerFree()))
                .status(MatchStatus.ACTIVE)
                .build()
                ;

        return plabMatch;
    }
}
