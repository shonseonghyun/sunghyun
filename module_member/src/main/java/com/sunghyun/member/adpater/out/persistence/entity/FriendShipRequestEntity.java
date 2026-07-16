package com.sunghyun.member.adpater.out.persistence.entity;

import com.sunghyun.member.domain.enums.FriendShipRequestStatus;
import com.sunghyun.member.domain.model.FriendShipRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "friendship_request")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FriendShipRequestEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long friendShipRequestNo;

    @Column
    private Long requesterMemberNo;

    @Column
    private Long receiverMemberNo;

    @Column
    @Enumerated(EnumType.STRING)
    private FriendShipRequestStatus status;

    @Column
    private String requestedDt;

    @Column
    private String requestedTm;

    @Column
    private String answeredDt;

    @Column
    private String answeredTm;

    public static FriendShipRequestEntity fromDomain(FriendShipRequest domain) {
        if (domain == null) {
            return null;
        }
        return FriendShipRequestEntity.builder()
                .friendShipRequestNo(domain.getFriendShipRequestNo())
                .requesterMemberNo(domain.getRequesterMemberNo())
                .receiverMemberNo(domain.getReceiverMemberNo())
                .status(domain.getStatus())
                .requestedDt(domain.getRequestedDt())
                .requestedTm(domain.getRequestedTm())
                .answeredDt(domain.getAnsweredDt())
                .answeredTm(domain.getAnsweredTm())
                .build();
    }

    public FriendShipRequest toDomain() {
        return FriendShipRequest.builder()
                .friendShipRequestNo(this.friendShipRequestNo)
                .requesterMemberNo(this.requesterMemberNo)
                .receiverMemberNo(this.receiverMemberNo)
                .status(this.status)
                .requestedDt(this.requestedDt)
                .requestedTm(this.requestedTm)
                .answeredDt(this.answeredDt)
                .answeredTm(this.answeredTm)
                .build();
    }
}