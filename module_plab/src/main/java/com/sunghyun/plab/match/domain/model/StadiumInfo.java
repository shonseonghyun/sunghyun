package com.sunghyun.plab.match.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "stadium_info")
@NoArgsConstructor
@AllArgsConstructor
public class StadiumInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long stadiumNo;

    @Column
    private String stadiumName;

    @Column
    private Integer maxApplyCount;

    @Column
    private String location;

    @Column
    private String stadiumImage;
}
