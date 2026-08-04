package com.sunghyun.web;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    // --- 공통 (S, F, C) ---
    S000(HttpStatus.OK, "요청에 성공하였습니다."),
    F000(HttpStatus.BAD_REQUEST, "실패"),
    F001(HttpStatus.BAD_REQUEST, "의도치 못한 예외가 발생하였습니다. 문의 바랍니다. "),
    C400(HttpStatus.BAD_REQUEST, "잘못된 요청 형식입니다."),      // Type Mismatch 등
    C404(HttpStatus.NOT_FOUND, "존재하지 않는 경로이거나 찾을 수 없는 리소스입니다."), // 405 Method Not Allowed
    C405(HttpStatus.METHOD_NOT_ALLOWED, "지원하지 않는 HTTP 메소드입니다."), // 405 Method Not Allowed
    C504(HttpStatus.OK, "타임아웃 발생하였습니다"),
    C000(HttpStatus.BAD_REQUEST, "잘못된 enum 코드입니다."),

    // --- 유저 관련 (M) ---
    M000(HttpStatus.NOT_FOUND, "존재하지 않는 회원입니다."),
    M001(HttpStatus.BAD_REQUEST, "이미 사용 중이거나 대기 중인 아이디입니다."),
    M002(HttpStatus.BAD_REQUEST, "사용 불가한 아이디입니다."),
    M003(HttpStatus.BAD_REQUEST, "아이디 중복 확인이 필요합니다."),
    M004(HttpStatus.UNAUTHORIZED, "인증 세션이 만료되었습니다. 다시 시도해주세요."),
    M005(HttpStatus.BAD_REQUEST, "현재 비밀번호가 일치하지 않습니다."),
    M006(HttpStatus.BAD_REQUEST, "기존 비밀번호와 동일한 비밀번호는 사용할 수 없습니다."),
    M007(HttpStatus.BAD_REQUEST, "아이디 또는 비밀번호를 확인해주세요."),
    M008(HttpStatus.BAD_REQUEST, "새로 바꾸실 비밀번호를 입력해주세요."),
    M009(HttpStatus.BAD_REQUEST, "재로그인 부탁드립니다."),
    M010(HttpStatus.BAD_REQUEST, "이미 친구 신청 요청된 상태입니다."),

    M011(HttpStatus.BAD_REQUEST, "이미 처리된 친구 요청이라 수락할 수 없습니다."),
    M012(HttpStatus.BAD_REQUEST, "수락 가능한 대기 상태가 아닙니다."),
    M013(HttpStatus.BAD_REQUEST, "대기 상태가 아니라서 거절할 수 없습니다."),
    M014(HttpStatus.BAD_REQUEST, "이미 상대방에 의해 처리된 요청이라 철회할 수 없습니다."),
    M015(HttpStatus.BAD_REQUEST, "애초에 친구 상태가 아니라서 끊을 수 없습니다."),
    M016(HttpStatus.BAD_REQUEST, "이미 끊어진 친구 관계입니다."),
    M017(HttpStatus.BAD_REQUEST, "자기 자신에게는 친구 신청을 할 수 없습니다."),

    // -HttpStatus.OK,-- 플랩 관련 (P) ---
    P01(HttpStatus.BAD_REQUEST, "이미 알림을 등록한 매치입니다."),
    P02(HttpStatus.BAD_REQUEST, "유효하지 않은 플랩 매치 번호입니다."),
    P03(HttpStatus.NOT_FOUND, "존재하지 않는 구독 매치입니다."),
    P04(HttpStatus.BAD_REQUEST, "모집 기간이 만료된 매치입니다"),
    P05(HttpStatus.BAD_REQUEST, "취소된 매치입니다."),

    // -HttpStatus.OK,-- 아웃박스 관련 (O) ---
    O000(HttpStatus.NOT_FOUND, "존재하지 않는 아웃박스입니다."),

    // -HttpStatus.OK,-- 채팅 (Z) ---
    Z000(HttpStatus.NOT_FOUND, "존재하지 않는 채팅방입니다."),
    Z001(HttpStatus.NOT_FOUND, "해당 채팅방에 참여한 회원인지 확인 바랍니다."),
    Z002(HttpStatus.BAD_REQUEST, "1:1 채팅방은 최대 2명까지만 참여할 수 있습니다."),
    Z003(HttpStatus.BAD_REQUEST, "채팅방은 최소 2명 이상 참여자가 존재해야 합니다."),
    Z004(HttpStatus.BAD_REQUEST, "단체 채팅방은 최소 3명 이상 참여자가 존재해야 합니다."),


    // -HttpStatus.OK,-- 인증,인가 (T) ---
    T00(HttpStatus.UNAUTHORIZED, "잘못된 JWT 서명 또는 손상된 토큰입니다."),
    T01(HttpStatus.UNAUTHORIZED, "만료된 JWT 토큰입니다."),
    T02(HttpStatus.UNAUTHORIZED, "지원되지 않는 형식의 JWT 토큰입니다."),
    T03(HttpStatus.BAD_REQUEST, "JWT 토큰의 클레임이 비어있거나 올바르지 않습니다."),
    T04(HttpStatus.FORBIDDEN, "토큰에 권한 정보가 유출되었거나 존재하지 않습니다."),

    T05(HttpStatus.FORBIDDEN, "해당 자원에 대한 접근 권한이 올바르지 않습니다."),
    T06(HttpStatus.UNAUTHORIZED, "로그인이 필요한 서비스입니다."),

    // -HttpStatus.OK,-- 시스템 및 기타 (G, P) ---
    G01(HttpStatus.OK, "데이터 머지 중 오류가 발생했습니다."), // '머지'보다는 범용적인 표현 권장
    G02(HttpStatus.OK, "클래스 타입이 일치하지 않습니다."), // '머지'보다는 범용적인 표현 권장

    // --- 외부 API 관련 ---
    E000(HttpStatus.INTERNAL_SERVER_ERROR, "외부 서비스 호출 중 오류가 발생했습니다."),
    E001(HttpStatus.NOT_FOUND, "찾을 수 없습니다."),
    E002(HttpStatus.INTERNAL_SERVER_ERROR, "메일 전송 실패하였습니다."), //흠 얘는 5xx로 응답해야 하는데..
    ;

    private final HttpStatus httpStatus;
    private final String message;
}