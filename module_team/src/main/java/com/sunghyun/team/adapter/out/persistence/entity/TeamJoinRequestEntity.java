package com.sunghyun.team.adapter.out.persistence.entity;

import com.sunghyun.team.domain.enums.TeamJoinRequestStatus;
import com.sunghyun.team.domain.model.TeamJoinRequest;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "team_join_request")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class TeamJoinRequestEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long requestNo;

    private Long teamNo;

    private Long memberNo;

    @Enumerated(EnumType.STRING)
    private TeamJoinRequestStatus status;

    private String createDt;
    private String createTm;

    private String modifiedDt;
    private String modifiedTm;

    // Entity -> Domain 변환
    public TeamJoinRequest toDomain() {
        return TeamJoinRequest.builder()
                .requestNo(this.requestNo)
                .teamNo(this.teamNo)
                .memberNo(this.memberNo)
                .status(this.status)
                .createDt(this.createDt)
                .createTm(this.createTm)
                .modifiedDt(this.modifiedDt)
                .modifiedTm(this.modifiedTm)
                .build();
    }

    // Domain -> Entity 변환
    public static TeamJoinRequestEntity fromDomain(TeamJoinRequest domain) {
        return TeamJoinRequestEntity.builder()
                .requestNo(domain.getRequestNo())
                .teamNo(domain.getTeamNo())
                .memberNo(domain.getMemberNo())
                .status(domain.getStatus())
                .createDt(domain.getCreateDt())
                .createTm(domain.getCreateTm())
                .modifiedDt(domain.getModifiedDt())
                .modifiedTm(domain.getModifiedTm())
                .build();
    }
}