package com.sunghyun.guest.domain;

public enum RecruitmentStatus {
    RECRUITING, // 모집중 = 전체 정원 내 자리 남은 포지션 존재
    COMPLETED, // 마감 = 전체 정원 참
    CANCELLED // 취소 = 요청자가 직접 취소함
}
