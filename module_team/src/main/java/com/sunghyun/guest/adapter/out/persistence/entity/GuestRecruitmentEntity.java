package com.sunghyun.guest.adapter.out.persistence.entity;

import com.sunghyun.guest.domain.*;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "guest_recruitment")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class GuestRecruitmentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long recruitmentNo;

    private Long memberNo;

    private String title;

    @Enumerated(EnumType.STRING)
    private RecruitmentStatus status;

    private Long schedulePollNo;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "recruitment_no",updatable = false,nullable = false)
    @Builder.Default
    private List<PositionRequirementEntity> requirements = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private LevelLimit levelLimit;

    @Enumerated(EnumType.STRING)
    private MatchFormat matchFormat;

    private int guestFee;

    private String playDt;
    private String playTm;

    private int durationMinute;
    private int quarterDuration;
    private int restDuration;
    private int minGuaranteedQuarter;

    private boolean isBeverageProvided;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "recruitment_no",updatable = false,nullable = false)
    @Builder.Default
    private List<GuestApplyerEntity> guestApplications = new ArrayList<>();

    @Column(columnDefinition = "TEXT")
    private String additionalNotice;

    private String createdDt;
    private String createdTm;
    private String modifiedDt;
    private String modifiedTm;

    public GuestRecruitment toDomain() {
        return GuestRecruitment.builder()
                .recruitmentNo(this.recruitmentNo)
                .memberNo(this.memberNo)
                .title(this.title)
                .status(this.status)
                .schedulePollNo(this.schedulePollNo)
                .requirements(
                        this.requirements == null ? new ArrayList<>() :
                                new ArrayList<>(this.requirements.stream().map(PositionRequirementEntity::toDomain).toList())
                )
                .levelLimit(this.levelLimit)
                .matchFormat(this.matchFormat)
                .guestFee(this.guestFee)
                .playDt(this.playDt)
                .playTm(this.playTm)
                .durationMinute(this.durationMinute)
                .quarterDuration(this.quarterDuration)
                .restDuration(this.restDuration)
                .minGuaranteedQuarter(this.minGuaranteedQuarter)
                .isBeverageProvided(this.isBeverageProvided)
                .guestApplyers(
                        this.guestApplications == null ? new ArrayList<>() :
                                new ArrayList<>(this.guestApplications.stream().map(GuestApplyerEntity::toDomain).toList()) // 👈 new ArrayList로 감싸서 가변 리스트로 생성
                )
                .additionalNotice(this.additionalNotice)
                .createdDt(this.createdDt)
                .createdTm(this.createdTm)
                .modifiedDt(this.modifiedDt)
                .modifiedTm(this.modifiedTm)
                .build();
    }

    public static GuestRecruitmentEntity fromDomain(GuestRecruitment domain) {
        if (domain == null) return null;

        List<PositionRequirementEntity> reqEntities = domain.getRequirements() == null ? Collections.emptyList() :
                domain.getRequirements().stream()
                        .map(PositionRequirementEntity::fromDomain)
                        .toList();

        List<GuestApplyerEntity> appEntities = domain.getGuestApplyers() == null ? Collections.emptyList() :
                domain.getGuestApplyers().stream()
                        .map(GuestApplyerEntity::fromDomain)
                        .toList();

        return GuestRecruitmentEntity.builder()
                .recruitmentNo(domain.getRecruitmentNo())
                .memberNo(domain.getMemberNo())
                .title(domain.getTitle())
                .status(domain.getStatus())
                .schedulePollNo(domain.getSchedulePollNo())
                .requirements(reqEntities)
                .levelLimit(domain.getLevelLimit())
                .matchFormat(domain.getMatchFormat())
                .guestFee(domain.getGuestFee())
                .playDt(domain.getPlayDt())
                .playTm(domain.getPlayTm())
                .durationMinute(domain.getDurationMinute())
                .quarterDuration(domain.getQuarterDuration())
                .restDuration(domain.getRestDuration())
                .minGuaranteedQuarter(domain.getMinGuaranteedQuarter())
                .isBeverageProvided(domain.isBeverageProvided())
                .guestApplications(appEntities)
                .additionalNotice(domain.getAdditionalNotice())
                .createdDt(domain.getCreatedDt())
                .createdTm(domain.getCreatedTm())
                .modifiedDt(domain.getModifiedDt())
                .modifiedTm(domain.getModifiedTm())
                .build();
    }
}