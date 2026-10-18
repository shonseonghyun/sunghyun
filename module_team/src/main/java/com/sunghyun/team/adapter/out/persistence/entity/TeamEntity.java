package com.sunghyun.team.adapter.out.persistence.entity;

import com.sunghyun.team.domain.enums.AgeGroup;
import com.sunghyun.team.domain.enums.GenderComposition;
import com.sunghyun.team.domain.enums.TeamType;
import com.sunghyun.team.domain.model.MonthlyFee;
import com.sunghyun.team.domain.model.Team;
import com.sunghyun.team.domain.model.TeamMember;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "team")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class TeamEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_no")
    private Long teamNo;

    @Column(name = "member_no")
    private Long creatorMemberNo;

    @Column(name = "team_name")
    private String teamName;

    @Enumerated(EnumType.STRING)
    @Column(name = "team_type", nullable = false, length = 20)
    private TeamType teamType;

    @Column(name = "logo_url", length = 500)
    private String logoUrl;

    @Column(name = "team_desc", columnDefinition = "TEXT")
    private String teamDesc;

    @Column(name = "activity_region")
    private String activityRegion;

    @Column(name = "frequent_stadium", length = 100)
    private String frequentStadium;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender_composition")
    private GenderComposition genderComposition;

    @Enumerated(EnumType.STRING)
    @Column(name = "main_age_group")
    private AgeGroup mainAgeGroup;

    @Embedded
    private MonthlyFeeEmbeddable monthlyFee;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "team_no", nullable = false)
    @Builder.Default
    private List<TeamMemberEntity> teamMemberList = new ArrayList<>();

    // Entity -> Domain 변환
    public Team toDomain() {
        MonthlyFee fee = null;
        if (this.monthlyFee != null && this.monthlyFee.getAmount() != null) {
            fee = new MonthlyFee(this.monthlyFee.getAmount(), this.monthlyFee.getPaymentDay());
        }

        List<TeamMember> members = this.teamMemberList != null ?
                this.teamMemberList.stream().map(TeamMemberEntity::toDomain).collect(Collectors.toList()) :
                new ArrayList<>();

        return Team.builder()
                .teamNo(this.teamNo)
                .creatorMemberNo(this.creatorMemberNo)
                .teamName(this.teamName)
                .teamType(this.teamType)
                .logoUrl(this.logoUrl)
                .teamDesc(this.teamDesc)
                .activityRegion(this.activityRegion)
                .frequentStadium(this.frequentStadium)
                .genderComposition(this.genderComposition)
                .mainAgeGroup(this.mainAgeGroup)
                .monthlyFee(fee)
                .teamMemberList(members)
                .build();
    }

    // Domain -> Entity 변환
    public static TeamEntity fromDomain(Team team) {
        MonthlyFeeEmbeddable feeEmbeddable = null;
        if (team.getMonthlyFee() != null) {
            feeEmbeddable = new MonthlyFeeEmbeddable(
                    team.getMonthlyFee().getAmount(),
                    team.getMonthlyFee().getPaymentDay()
            );
        }

        List<TeamMemberEntity> memberEntities = team.getTeamMemberList() != null ?
                team.getTeamMemberList().stream().map(TeamMemberEntity::fromDomain).collect(Collectors.toList()) :
                new ArrayList<>();

        return TeamEntity.builder()
                .teamNo(team.getTeamNo())
                .creatorMemberNo(team.getCreatorMemberNo())
                .teamName(team.getTeamName())
                .teamType(team.getTeamType())
                .logoUrl(team.getLogoUrl())
                .teamDesc(team.getTeamDesc())
                .activityRegion(team.getActivityRegion())
                .frequentStadium(team.getFrequentStadium())
                .genderComposition(team.getGenderComposition())
                .mainAgeGroup(team.getMainAgeGroup())
                .monthlyFee(feeEmbeddable)
                .teamMemberList(memberEntities)
                .build();
    }
}