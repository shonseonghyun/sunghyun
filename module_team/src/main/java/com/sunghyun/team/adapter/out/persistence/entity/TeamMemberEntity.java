package com.sunghyun.team.adapter.out.persistence.entity;

import com.sunghyun.team.domain.enums.TeamMemberRole;
import com.sunghyun.team.domain.model.TeamMember;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "team_member")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class TeamMemberEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_member_no")
    private Long teamMemberNo;

    @Column(name = "member_no")
    private Long realMemberNo;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private TeamMemberRole teamMemberRole;

    private String createdDt;
    private String createdTm;

    // Entity -> Domain 변환
    public TeamMember toDomain() {
        return TeamMember.builder()
                .teamMemberNo(this.teamMemberNo)
                .realMemberNo(this.realMemberNo)
                .teamMemberRole(this.teamMemberRole)
                .createdDt(this.createdDt)
                .createdTm(this.createdTm)
                .build();
    }

    // Domain -> Entity 변환
    public static TeamMemberEntity fromDomain(TeamMember domain) {
        return TeamMemberEntity.builder()
                .teamMemberNo(domain.getTeamMemberNo())
                .realMemberNo(domain.getRealMemberNo())
                .teamMemberRole(domain.getTeamMemberRole())
                .createdDt(domain.getCreatedDt())
                .createdTm(domain.getCreatedTm())
                .build();
    }
}