package com.sunghyun.schedule.adapter.out.persistence.entity;

import com.sunghyun.schedule.domain.PlayerLimit;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PlayerLimitEmbeddable {

    @Column(name = "min_player_cnt")
    private Integer min;

    @Column(name = "max_player_cnt")
    private Integer max;

    public static PlayerLimitEmbeddable from(PlayerLimit domain) {
        if (domain == null) return null;
        return new PlayerLimitEmbeddable(domain.getMin(), domain.getMax());
    }

    public PlayerLimit toDomain() {
        return new PlayerLimit(this.min, this.max);
    }
}