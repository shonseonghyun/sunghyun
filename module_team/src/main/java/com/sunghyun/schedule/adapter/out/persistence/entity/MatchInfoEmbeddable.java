package com.sunghyun.schedule.adapter.out.persistence.entity;

import com.sunghyun.schedule.domain.Location;
import com.sunghyun.schedule.domain.MatchInfo;
import com.sunghyun.schedule.domain.enums.MatchType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MatchInfoEmbeddable {

    @Enumerated(EnumType.STRING)
    @Column(name = "match_type")
    private MatchType matchType;

//    @OneToOne
//    @Column(name = "location")
//    private LocationEntity location;

    //엔티티 연관관계 대신 외래키 컬럼 매핑
    @Column
    private Long locationNo;

    @Column(name = "play_dt")
    private String playDt;

    @Column(name = "play_tm")
    private String playTm;

    // Domain -> Entity
    public static MatchInfoEmbeddable from(MatchInfo domain) {
        if (domain == null) return null;
        return new MatchInfoEmbeddable(
                domain.getMatchType(),
                // 장소 미정인 채 들어온 경우 방어코딩
                domain.getLocation() == null ? null : domain.getLocation().getLocationNo(),
                domain.getPlayDt(),
                domain.getPlayTm()
        );
    }

    // Entity -> Domain
    public MatchInfo toDomain() {
        return new MatchInfo(
                this.matchType,
                // 장소 미정인 채 나가는 경우 방어코딩
                this.locationNo == null ? null : new Location(this.locationNo),
                this.playDt,
                this.playTm
        );
    }
}