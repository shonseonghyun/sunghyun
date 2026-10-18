package com.sunghyun.guest.adapter.out.persistence.entity;

import com.sunghyun.guest.domain.ApplicationStatus;
import com.sunghyun.guest.domain.GuestApplyer;
import com.sunghyun.guest.domain.Position;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "guest_applyer")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class GuestApplyerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long applicationNo;

    private Long memberNo;

    @Enumerated(EnumType.STRING)
    private ApplicationStatus applicationStatus;

    @Enumerated(EnumType.STRING)
    private Position position;

    private String createdDt;
    private String createdTm;
    private String modifiedDt;
    private String modifiedTm;

    // Entity -> Domain
    public GuestApplyer toDomain() {
        return GuestApplyer.builder()
                .applicationNo(this.applicationNo)
                .memberNo(this.memberNo)
                .position(this.position)
                .applicationStatus(this.applicationStatus)
                .createdDt(this.createdDt)
                .createdTm(this.createdTm)
                .modifiedDt(this.modifiedDt)
                .modifiedTm(this.modifiedTm)
                .build();
    }

    // Domain -> Entity
    public static GuestApplyerEntity fromDomain(GuestApplyer domain) {
        return GuestApplyerEntity.builder()
                .applicationNo(domain.getApplicationNo())
                .memberNo(domain.getMemberNo())
                .applicationStatus(domain.getApplicationStatus())
                .position(domain.getPosition())
                .createdDt(domain.getCreatedDt())
                .createdTm(domain.getCreatedTm())
                .modifiedDt(domain.getModifiedDt())
                .modifiedTm(domain.getModifiedTm())
                .build();
    }
}