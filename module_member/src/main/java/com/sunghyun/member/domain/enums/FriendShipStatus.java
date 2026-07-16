package com.sunghyun.member.domain.enums;

public enum FriendShipStatus {
    REQUESTED,  // 친구 요청 보냄 (대기 중)
    ACCEPTED,   // 친구 수락 완료 (채팅 가능)
    REJECTED    // 친구 요청 거절됨
}
