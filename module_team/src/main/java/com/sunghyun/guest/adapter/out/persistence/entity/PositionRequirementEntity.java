package com.sunghyun.guest.adapter.out.persistence.entity;

import com.sunghyun.guest.domain.Position;
import com.sunghyun.guest.domain.PositionRequirement;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "position_requirement")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class PositionRequirementEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long requirementNo;

    @Enumerated(EnumType.STRING)
    private Position position;

    private int requiredCount;

    // Entity -> Domain
    public PositionRequirement toDomain() {
        return PositionRequirement.builder()
                .requirementNo(this.requirementNo)
                .position(this.position)
                .requiredCount(this.requiredCount)
                .build();
    }

    // Domain -> Entity
    public static PositionRequirementEntity fromDomain(PositionRequirement domain) {
        if (domain == null) return null;
        return PositionRequirementEntity.builder()
                .requirementNo(domain.getRequirementNo())
                .position(domain.getPosition())
                .requiredCount(domain.getRequiredCount())
                .build();
    }
}