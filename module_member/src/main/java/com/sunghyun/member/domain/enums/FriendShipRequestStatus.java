package com.sunghyun.member.domain.enums;

public enum FriendShipRequestStatus {
    REQUESTED, // 상대방이 신청(대기 중)
    REJECTED,  // 상대방이 거절
    ACCEPTED,  // 상대방이 수락(친구)
    CANCELLED, // 수락 대기 중인 신청을 요청자가 철회
    UNFRIENDED, // 이미 친구인 상태에서  끊음
}
