package com.sunghyun.web;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    // --- 공통 (S, F, C) ---
    S00("요청에 성공하였습니다."),
    F00("실패"),
    C01("잘못된 요청 형식입니다."),      // Type Mismatch 등
    COMMON_404("존재하지 않는 경로입니다."), // 405 Method Not Allowed
    COMMON_405("지원하지 않는 요청 메소드입니다."), // 405 Method Not Allowed

    // --- 유저 관련 (M) ---
    M00("존재하지 않는 회원입니다."),
    M01("이미 사용 중이거나 대기 중인 아이디입니다."),
    M02("이미 가입된 아이디입니다."),
    M03("아이디 중복 확인이 필요합니다."),
    M04("인증 세션이 만료되었습니다. 다시 시도해주세요."),
    M05("현재 비밀번호가 일치하지 않습니다."),
    M06("기존 비밀번호와 동일한 비밀번호는 사용할 수 없습니다."),

    // --- 플랩 관련 (P) ---
    P01("이미 알림을 등록한 매치입니다."),
    P02("유효하지 않은 플랩 매치 번호입니다."),
    P03("존재하지 않는 구독 매치입니다."),
    P04("모집 기간이 만료된 매치입니다"),


    // --- 시스템 및 기타 (G, P) ---
    G01("데이터 머지 중 오류가 발생했습니다."), // '머지'보다는 범용적인 표현 권장
    G02("클래스 타입이 일치하지 않습니다."), // '머지'보다는 범용적인 표현 권장

    // --- 외부 API 관련 ---
    EXTERNAL_API_ERROR("외부 서비스 호출 중 오류가 발생했습니다."),
    COMMON_400("찾을 수 없습니다."),


    ;

    private final String message;
}
