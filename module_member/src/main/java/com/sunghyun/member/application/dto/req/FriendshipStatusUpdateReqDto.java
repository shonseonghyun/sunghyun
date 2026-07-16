package com.sunghyun.member.application.dto.req;

import com.sunghyun.member.domain.enums.FriendShipRequestStatus;
import lombok.Getter;

@Getter
public class FriendshipStatusUpdateReqDto {
    private FriendShipRequestStatus status;
}
