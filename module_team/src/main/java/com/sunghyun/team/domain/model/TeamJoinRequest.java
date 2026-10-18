package com.sunghyun.team.domain.model;

import com.sunghyun.team.domain.enums.TeamJoinRequestStatus;
import com.sunghyun.team.domain.exceptions.TeamJoinRequestException;
import com.sunghyun.utils.ApiUtils;
import com.sunghyun.web.ErrorCode;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class TeamJoinRequest {
    private Long requestNo;
    private Long teamNo;
    private Long memberNo;
    private TeamJoinRequestStatus status;
    private String createDt;
    private String createTm;
    private String modifiedDt;
    private String modifiedTm;

    public static TeamJoinRequest create(Long teamNo, Long memberNo) {
        return TeamJoinRequest.builder()
                .teamNo(teamNo)
                .memberNo(memberNo)
                .status(TeamJoinRequestStatus.REQUESTED)
                .createDt(ApiUtils.getCurrentDt())
                .createTm(ApiUtils.getCurrentTm())
                .build();
    }

    public Long accept() {
        changeStatus(TeamJoinRequestStatus.ACCEPTED);
        return this.memberNo;
    }

    public void reject() {
        changeStatus(TeamJoinRequestStatus.REJECTED);
    }

    public void cancel(Long requesterMemberNo) {
        validateRequester(requesterMemberNo);
        changeStatus(TeamJoinRequestStatus.CANCELLED);
    }

    // --- Private Helper Methods ---

    /**
     * 상태 변경 공통 메서드 (검증 + 상태 변경 + 수정일시 갱신)
     */
    private void changeStatus(TeamJoinRequestStatus newStatus) {
        if (this.status != TeamJoinRequestStatus.REQUESTED) {
            throw new TeamJoinRequestException(ErrorCode.T004);
        }
        this.status = newStatus;
        setModifiedDtTm();
    }

    /**
     * 신청 당사자 검증
     */
    private void validateRequester(Long requesterMemberNo) {
        if (!this.memberNo.equals(requesterMemberNo)) {
            throw new TeamJoinRequestException(ErrorCode.T005);
        }
    }

    private void setModifiedDtTm() {
        this.modifiedDt = ApiUtils.getCurrentDt();
        this.modifiedTm = ApiUtils.getCurrentTm();
    }
}