package com.sunghyun.member.domain.model;

import com.sunghyun.member.domain.enums.FriendShipStatus;

public class FriendShip {
    private Long friendShipNo;

    private Long receiverMemberNo;
    private Long requesterMemberNo;

    private FriendShipStatus status;

    private String RequestedDt;
    private String RequestedTm;

    private String AnsweredDt;
    private String AnsweredTm;
}
