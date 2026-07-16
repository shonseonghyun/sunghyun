package com.sunghyun.member.domain.model;

import com.sunghyun.member.domain.enums.FriendShipRequestStatus;
import com.sunghyun.member.domain.exception.friendship.*;
import com.sunghyun.utils.ApiUtils;
import com.sunghyun.web.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class FriendShipRequest {
    private Long friendShipRequestNo;

    private Long requesterMemberNo;
    private Long receiverMemberNo;

    private FriendShipRequestStatus status;

    private String requestedDt;
    private String requestedTm;

    private String answeredDt;
    private String answeredTm;

    public static FriendShipRequest create(Long requesterMemberNo, Long receiverMemberNo) {
        if (requesterMemberNo.equals(receiverMemberNo)) {
            throw new SelfFriendRequestException(ErrorCode.M017);
        }
        return FriendShipRequest.builder()
                .requesterMemberNo(requesterMemberNo)
                .receiverMemberNo(receiverMemberNo)
                .status(FriendShipRequestStatus.REQUESTED)
                .requestedDt(ApiUtils.getCurrentDt())
                .requestedTm(ApiUtils.getCurrentTm())
                .build()
                ;
    }

    public void updateStatus(FriendShipRequestStatus status) {
        switch (status) {
            case ACCEPTED -> accept();
            case REJECTED -> reject();
            case CANCELLED -> cancel();
            case UNFRIENDED -> unfriend();
        }
    }

    // 1. 수락 (상대방)
    private void accept() {
        // 이미 처리 완료된 상태(수락/거절/철회)인지 먼저 체크
        if (this.status == FriendShipRequestStatus.ACCEPTED ||
                this.status == FriendShipRequestStatus.REJECTED ||
                this.status == FriendShipRequestStatus.CANCELLED) {
            throw new FriendShipAlreadyProcessedException(ErrorCode.M011);
        }

        // 대기 상태(REQUESTED)가 아닐 때 던지는 일반 수락 에러
        if (this.status != FriendShipRequestStatus.REQUESTED) {
            throw new InvalidFriendShipAcceptException(ErrorCode.M012);
        }

        this.status = FriendShipRequestStatus.ACCEPTED;
        this.answeredDt = ApiUtils.getCurrentDt();
        this.answeredTm = ApiUtils.getCurrentTm();
    }

    // 2. 거절 (상대방)
    private void reject() {
        if (this.status != FriendShipRequestStatus.REQUESTED) {
            throw new InvalidFriendShipRejectException(ErrorCode.M013);
        }

        this.status = FriendShipRequestStatus.REJECTED;
        this.answeredDt = ApiUtils.getCurrentDt();
        this.answeredTm = ApiUtils.getCurrentTm();
    }

    // 3. 신청 철회 (요청자)
    private void cancel() {
        // 상대가 이미 수락했거나 거절했으면 철회 불가능
        if (this.status == FriendShipRequestStatus.ACCEPTED ||
                this.status == FriendShipRequestStatus.REJECTED) {
            throw new InvalidFriendShipCancelException(ErrorCode.M014);
        }

        this.status = FriendShipRequestStatus.CANCELLED;
    }

    // 4. 친구 끊기 (둘 중 아무나)
    private void unfriend() {
        // 이미 끊어진 상태인 경우
        if (this.status == FriendShipRequestStatus.UNFRIENDED) {
            throw new FriendShipAlreadyDisconnectedException(ErrorCode.M016);
        }

        // 애초에 친구 상태(ACCEPTED)가 아니었던 경우
        if (this.status != FriendShipRequestStatus.ACCEPTED) {
            throw new NotFriendShipStatusException(ErrorCode.M015);
        }

        this.status = FriendShipRequestStatus.UNFRIENDED;
    }
}